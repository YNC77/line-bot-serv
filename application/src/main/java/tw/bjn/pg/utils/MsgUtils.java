package tw.bjn.pg.utils;

import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;
import tw.bjn.pg.event.handlers.TextEventHandler;

@Component
public class MsgUtils {

    public TextMessage createTextMsg(String msg) {
        return new TextMessage(msg);
    }
}
