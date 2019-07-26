package tw.bjn.pg.event.handlers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
//import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;

//@LineEventHandler("message")
public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    public TextEventHandler(LineBotUtils lineBotUtils) {
        super(lineBotUtils);
    }

    @Override
    public void onEvent(MessageEvent<TextMessageContent> event) {
    }
}
