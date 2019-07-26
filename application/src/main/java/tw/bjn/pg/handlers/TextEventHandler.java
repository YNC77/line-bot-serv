package tw.bjn.pg.handlers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import tw.bjn.pg.interfaces.EventHandler;

public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    public TextEventHandler(LineMessagingClient lineMessagingClient) {
        super(lineMessagingClient);
    }

    @Override
    public void handle(MessageEvent<TextMessageContent> event) {
    }
}
