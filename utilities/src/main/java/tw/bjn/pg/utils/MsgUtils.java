package tw.bjn.pg.utils;

import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;

@Component
public class MsgUtils {

    public TextMessage createTextMsg(String msg) {
        return new TextMessage(msg);
    }
}
