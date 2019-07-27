package tw.bjn.pg;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tw.bjn.pg.event.handlers.TextEventHandler;
import tw.bjn.pg.interfaces.event.EventDispatcher;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {

    @Autowired
    private EventDispatcher dispatcher;

    @Autowired
    private TextEventHandler textEventHandler;

    // Todo: test infra, dummy server...

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
        Map<String, List<String>> q = textEventHandler.getQuotations();
        String s = textEventHandler.getReplySentence();

        System.out.println("ss");
    }
}
