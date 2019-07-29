package tw.bjn.pg;

import com.linecorp.bot.model.event.message.TextMessageContent;
import org.junit.Ignore;
import org.junit.Test;

public class MessageControllerTest extends ControllerTest{

    @Ignore
    @Test
    public void testReplyMessage() {
        String json = requestFactory.createMessageRequest(
                new TextMessageContent("msg-id", "test text content"));
        String signature = requestFactory.getBotSignature(json.getBytes());
        String response = api.callback(json, signature);
        // TODO: validation
    }
}
