package tw.bjn.pg.handlers.message.text.helpers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.calculator.Calculator;
import tw.bjn.pg.utils.MsgUtils;

import java.util.regex.Pattern;

@Slf4j
@Component
public class Calculating implements Skill {

    @Autowired
    Calculator calculator;

    @Autowired
    MsgUtils msgUtils;

    private Pattern p = Pattern.compile("[0-9+\\-*/()\\s]+");

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        return p.matcher(event.getMessage().getText()).matches();
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        try {
            int result = calculator.calc(event.getMessage().getText());
            return msgUtils.createTextMsg(String.valueOf(result));
        } catch (Exception e) {
            log.error("failed to calculate", e);
            return null;
        }
    }

    @Override
    public int priority() {
        return 90;
    }
}
