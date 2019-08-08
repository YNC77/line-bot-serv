package tw.bjn.pg.ptt;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Ptt {

    private CookieStore cookieStore;
    private CloseableHttpClient httpClient;
    private final String PTT_HOST = "www.ptt.cc";
    private final String URL_SCHEME = "https://";
    private final String PTT_BASE_URL = URL_SCHEME + PTT_HOST;

    @PostConstruct
    public void init() {
        cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("over18", "1");
        cookie.setDomain(PTT_HOST);
        cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
        cookieStore.addCookie(cookie);
        httpClient = createHttpClient();
    }

    private CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }

    private String createUrlByBoardAndIndex(String board, String index) {
        // TODO: URL generator?
        StringBuilder builder = new StringBuilder();
        builder.append(PTT_BASE_URL)
                .append("/bbs");
        if (!StringUtils.isEmpty(board))
            builder.append("/").append(board);
        builder.append("/index");
        if (!index.isEmpty()) // supposed to be numeric
            builder.append(index);
        builder.append(".html");
        return builder.toString();
    }

    private String fetchFromPttWeb(String board, String index) {
        try {
            HttpGet g = new HttpGet(createUrlByBoardAndIndex(board, index));
            HttpResponse r = httpClient.execute(g);
            HttpEntity entity = r.getEntity();
            String html = EntityUtils.toString(entity);
            log.debug(html);
            return html;
        } catch (IOException e) {
            log.error("failed to get entry", e);
            throw new RuntimeException("failed to fetch ptt data.", e);
        }
    }

    public PttResult fetchHotBoards() {
        String html = fetchFromPttWeb("", "");
        Document doc = Jsoup.parse(html);
        List<PttBoardItem> boards = parseBoardItems(doc);
        return PttResult.builder()
                .pttBoardItems(boards)
                .build();
    }

    public PttResult fetchPttBoard(String board, String index) {
        String html = fetchFromPttWeb(board, index);
        PttResult.PttResultBuilder builder = PttResult.builder().board(board);
        Document doc = Jsoup.parse(html);
        List<PttPostItem> items = parsePostItems(doc);
        if (!CollectionUtils.isEmpty(items)) {
            builder.pttItemList(items);
        }
        List<String> navs = parseNavigation(doc);
        if (navs.size() == 4) {
            builder.oldestUrl(navs.get(0));
            builder.prevPageUrl(navs.get(1));
            builder.nextPageUrl(navs.get(2));
            builder.latestUrl(navs.get(3));
        }
        return builder.build();
    }

    private List<String> parseNavigation(Document doc) {
        Elements elements = doc.select("a.btn.wide");
        if (elements.isEmpty())
            return Collections.emptyList();
        return elements.stream()
                .map(e -> {
                    String url = e.attr("href");
                    return (url.isEmpty()) ? url : PTT_BASE_URL+url;
                })
                .collect(Collectors.toList());
    }

    private List<PttBoardItem> parseBoardItems(Document doc) {
        List<PttBoardItem> result = new ArrayList<>();
        for (Element el : doc.select("div.b-ent")) {
            result.add(
                    PttBoardItem.builder()
                            .name(el.selectFirst("div.board-name").text())
                            .popularity(el.selectFirst("div.board-nuser").text())
                            .clazz(el.selectFirst("div.board-class").text())
                            .title(el.selectFirst("div.board-title").text())
                            .build()
            );
        }
        return result;
    }

    private List<PttPostItem> parsePostItems(Document doc) {
        List<PttPostItem> result = new ArrayList<>();
        Element posts = doc.selectFirst("div.r-list-container");
        for (Element el : posts.children()) {
            if (el.is("div.r-list-sep"))
                break;
            if (el.is("div.r-ent")) {
                String author = null, url = null, title = null, date = null, reply = " ";

//                Element entity = el.selectFirst("div.title");
                Element entity = el.selectFirst("a");
                if (entity != null) {
                    title = entity.text();
                    url = entity.attr("href");
                }

                entity = el.selectFirst("span");
                if (entity != null)
                    reply = entity.text();

                entity = el.selectFirst("div.meta");
                if (entity != null) {
                    date = entity.selectFirst("div.date").text().trim();
                    author = entity.selectFirst("div.author").text();
                }

                if (title != null && url != null) {
                    result.add(new PttPostItem(title, PTT_BASE_URL+url, author, date, reply));
                }
            }
        }
        log.debug(Arrays.toString(result.toArray()));
        return result;
    }
}
