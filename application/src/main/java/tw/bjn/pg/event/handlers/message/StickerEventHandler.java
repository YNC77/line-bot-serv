package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventHandler;

@Component
public class StickerEventHandler extends EventHandler<MessageEvent<StickerMessageContent>> {
    @Override
    protected Message onEvent(MessageEvent<StickerMessageContent> event) {
        return new TextMessage(
                "Package: '"+ event.getMessage().getPackageId()+"' - '"+event.getMessage().getStickerId()+"'");
    }
}
