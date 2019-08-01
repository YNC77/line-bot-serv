package tw.bjn.pg;


import feign.Feign;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tw.bjn.pg.api.BotApi;
import tw.bjn.pg.callbacks.CallbackRequestFactory;

import javax.annotation.PostConstruct;

@Ignore
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected CallbackRequestFactory requestFactory;

    protected BotApi api;

    @PostConstruct
    public void init() {
        api  = Feign.builder()
                .target(BotApi.class, "http://0.0.0.0:" + port);
    }

}
