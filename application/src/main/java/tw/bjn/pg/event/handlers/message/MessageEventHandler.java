package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;

@Slf4j
@LineEventHandler("message")
public class MessageEventHandler extends EventHandler<MessageEvent> {

    @Autowired
    MessageEventHandler(LineBotUtils lineBotUtils) {
        super(lineBotUtils);
    }

    @Override
    protected Message onEvent(MessageEvent event) {
        // TODO: handle different sub type of message event
        MessageContent content = event.getMessage();
        if (content instanceof TextMessageContent) {
            final String text = ((TextMessageContent) content).getText();
            return new TextMessage(text);
        }
        return new TextMessage(content.getId());
    }
}
