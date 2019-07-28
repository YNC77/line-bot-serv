package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import tw.bjn.pg.interfaces.event.EventHandler;

public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        return new TextMessage(event.getMessage().getText());
    }
}
