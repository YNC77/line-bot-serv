package tw.bjn.pg;

import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.junit.Test;

public class MessageControllerTest extends ControllerTest{

    @Test
    public void testTextMessage() {
        String json = requestFactory.createMessageRequest(
                new TextMessageContent("msg-id", "test text content"));
        String signature = requestFactory.getBotSignature(json.getBytes());
        String response = api.callback(json, signature);
        // TODO: validation
    }

    @Test
    public void testStickerMessage() {
        String json = requestFactory.createMessageRequest(
                new StickerMessageContent("msg_id", "pack_id", "sticker_id"));
        String signature = requestFactory.getBotSignature(json.getBytes());
        String response = api.callback(json, signature);
        // TODO: validation
    }

    @Test
    public void testFollowEvent() {
        String json = requestFactory.createFollowRequest();
        String signature = requestFactory.getBotSignature(json.getBytes());
        String response = api.callback(json, signature);
        // TODO: validation
    }

    @Test
    public void testJoinEvent() {
        String json = requestFactory.createJoinRequest();
        String signature = requestFactory.getBotSignature(json.getBytes());
        String response = api.callback(json, signature);
        // TODO: validation
    }

    @Test
    public void testThingsEvent() {
        String json = requestFactory.createThingsRequest();
        String signature = requestFactory.getBotSignature(json.getBytes());
        String response = api.callback(json, signature);
        // TODO: validation
    }
}
