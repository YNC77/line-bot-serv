package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
//import tw.bjn.pg.annotations.LineEventHandler;
import com.linecorp.bot.model.message.Message;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;

//@LineEventHandler("message")
public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    public TextEventHandler(LineBotUtils lineBotUtils) {
        super(lineBotUtils);
    }

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        return null;
    }
}
