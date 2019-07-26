package tw.bjn.pg;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tw.bjn.pg.interfaces.event.EventDispatcher;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {

    @Autowired
    private EventDispatcher dispatcher;

    // Todo: test infra, dummy server...

    @Ignore
    @Test
    public void test() throws Exception {

//        MessageController messageController = new MessageController();
//        FollowController followController = new FollowController( dispatcher );
//
//        messageController.handleTextMessageEvent(
//                new MessageEvent<>("token", new UserSource("U123"),
//                        new TextMessageContent("id", "test text"), Instant.now()));
//
//        followController.handleFollowEvent(
//                new FollowEvent( "",
//                        new UserSource("U123") ,
//                        Instant.now())
//        );

    }
}
