package tw.bjn.pg.ptt.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import tw.bjn.pg.ptt.PttUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PttClientFactory {

    private OkHttpClient client;
    private Retrofit retrofit;
    private ConnectionPool connectionPool;

    @PostConstruct
    public void init() {
        connectionPool = new ConnectionPool(1, 30, TimeUnit.MINUTES);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::info);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .connectionPool(connectionPool)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .cookieJar(new CookieJar() {
                    List<Cookie> cookies = new ArrayList<Cookie>() {{
                            add(new Cookie.Builder()
                                .domain(PttUtils.PTT_HOST)
                                .name("over18")
                                .value("1")
                                .build());
                    }};
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        this.cookies.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookies;
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(new HttpUrl.Builder()
                        .scheme(PttUtils.URL_SCHEME)
                        .host(PttUtils.PTT_HOST)
                        .build().toString())
                .client(client)
                .build();
    }

    public PttWebApi create() {
        return retrofit.create(PttWebApi.class);
    }
}
