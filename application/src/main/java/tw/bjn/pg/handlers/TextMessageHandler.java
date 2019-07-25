package tw.bjn.pg.handlers;

//import com.linecorp.bot.model.event.message.TextMessageContent;
//import com.linecorp.bot.model.message.Message;
//import com.linecorp.bot.model.message.TextMessage;
//import org.springframework.stereotype.Component;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import tw.bjn.pg.interfaces.MessageHandler;

public class TextMessageHandler implements MessageHandler<MessageEvent<TextMessageContent>> {
    @Override
    public Message handle(MessageEvent<TextMessageContent> event) {
        return new TextMessage(event.getMessage().getText());
    }
}
