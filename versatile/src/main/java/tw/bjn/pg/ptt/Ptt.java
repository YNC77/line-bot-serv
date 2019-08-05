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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Ptt {

    private CookieStore cookieStore;
    private final String PTT_HOST = "www.ptt.cc";

    @PostConstruct
    public void init() {
        cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("over18", "1");
        cookie.setDomain(PTT_HOST);
        cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
        cookieStore.addCookie(cookie);
    }

    private CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }

    public List<PttItem> getLatest() {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            HttpGet g = new HttpGet("https://"+PTT_HOST+"/bbs/Gossiping/index.html");
            HttpResponse r = httpClient.execute(g);
            HttpEntity entity = r.getEntity();
            String html = EntityUtils.toString(entity);
            log.debug(html);
            return parseItem(html);
        } catch (IOException e) {
            log.error("failed to get entry", e);
            return null;
        }
    }

    private List<PttItem> parseItem(String html) {
        List<PttItem> result = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element posts = doc.selectFirst("div.r-list-container");
        for (Element el : posts.children()) {
            if (el.is("div.r-list-sep"))
                break;
            if (el.is("div.r-ent")) {
                for (Element entity : el.select("div.title")) {
                    log.debug(entity.toString());
                    Element a = entity.selectFirst("a");
                    if (a != null)
                        result.add(new PttItem(a.text(), "https://"+PTT_HOST+a.attr("href")));
                }
            }
        }
        return result;
    }
}
