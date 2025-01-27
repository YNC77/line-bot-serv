package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;

@Slf4j
@LineEventHandler("message")
public class MessageEventWrapper extends EventHandler<MessageEvent> {

    private TextEventHandler textEventHandler;
    private StickerEventHandler stickerEventHandler;

    @Autowired
    MessageEventWrapper(
            TextEventHandler textEventHandler,
            StickerEventHandler stickerEventHandler
    ) {
        this.textEventHandler = textEventHandler;
        this.stickerEventHandler = stickerEventHandler;
    }

    @Override
    protected Message onEvent(MessageEvent event) {
        // TODO: handle different sub type of message event
        MessageContent content = event.getMessage();
        if (content instanceof TextMessageContent) {
            return textEventHandler.onEvent(event);
        } else if (content instanceof StickerMessageContent) {
            return stickerEventHandler.onEvent(event);
        }
        return new TextMessage(content.getId());
    }
}
