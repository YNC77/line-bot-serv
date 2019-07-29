package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventHandler;

@Component
public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        return new TextMessage(event.getMessage().getText());
    }
}
