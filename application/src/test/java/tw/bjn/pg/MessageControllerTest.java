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

        String jsn = requestFactory.createMessageRequest(
                new TextMessageContent("msg-id", " $3020 "));
        String sgnature = requestFactory.getBotSignature(jsn.getBytes());
        api.callback(jsn, sgnature);
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

    @Test
    public void tsetp() throws InterruptedException {
        String jsn = requestFactory.createMessageRequest(
                new TextMessageContent("msg-id", "@ptt"));
        String sgnature = requestFactory.getBotSignature(jsn.getBytes());
        api.callback(jsn, sgnature);

        Thread.sleep(10000);
    }
}
