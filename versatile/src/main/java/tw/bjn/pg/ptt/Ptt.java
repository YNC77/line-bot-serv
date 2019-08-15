package tw.bjn.pg.ptt;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tw.bjn.pg.ptt.client.PttClientFactory;
import tw.bjn.pg.ptt.client.PttWebApi;
import tw.bjn.pg.ptt.model.PttBoardItem;
import tw.bjn.pg.ptt.model.PttPostItem;
import tw.bjn.pg.ptt.model.PttResult;

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

    private PttWebApi pttWebApi;

    private PttClientFactory pttClientFactory;

    @Autowired
    public Ptt (PttClientFactory pttClientFactory) {
        this.pttClientFactory = pttClientFactory;
    }

    @PostConstruct
    public void init() {
        pttWebApi = pttClientFactory.create();
    }

    public PttResult fetchHotBoards() {
        try {
            String html = pttWebApi.getHotBoards().execute().body().string();
            Document doc = Jsoup.parse(html);
            List<PttBoardItem> boards = parseBoardItems(doc);
            return PttResult.builder()
                    .pttBoardItems(boards)
                    .build();
        } catch (IOException e) {
            log.error("failed fetch", e);
            return null;
        }
    }

    public PttResult fetchPttFromURL(String text) {
        try {
            String url = text.trim();
            if (url.startsWith("/"))
                url = url.substring(1);
            String html = pttWebApi.fetchUrl(url).execute().body().string();
            return parsePttHtml(html, null);
        } catch (IOException e) {
            log.error("fetch failed.", e);
            return null;
        }
    }

    public PttResult fetchPttBoard(String board) {
        try {
            String html = pttWebApi.getPosts(board, "").execute().body().string();
            return parsePttHtml(html, board);
        } catch (IOException e) {
            log.error("fetch failed.", e);
            return null;
        }
    }

    public PttResult searchPttBoard(String board, String q) {
        try {
            String html = pttWebApi.searchBoard(board, q, null).execute().body().string();
            return parsePttHtml(html, board);
        } catch (IOException e) {
            log.error("fetch failed.", e);
            return null;
        }
    }

    /*
     maybe a Html to POJO converter
     */

    private PttResult parsePttHtml(String html, String board) {
        PttResult.PttResultBuilder builder = PttResult.builder();
        if (!StringUtils.isEmpty(board))
            builder.board(board);
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
                .map(e -> e.attr("href"))
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
                    result.add(
                             PttPostItem.builder()
                                     .title(title)
                                     .author(author)
                                     .date(date)
                                     .reply(reply)
                                     .url(new HttpUrl.Builder()
                                             .scheme(PttUtils.URL_SCHEME)
                                             .host(PttUtils.PTT_HOST)
                                             .addPathSegments(url) // will have additional '/'
                                             .build()
                                             .toString())
                                     .build());
                }
            }
        }
        log.debug(Arrays.toString(result.toArray()));
        return result;
    }
}
