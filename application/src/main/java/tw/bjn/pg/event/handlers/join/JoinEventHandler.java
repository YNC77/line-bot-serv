package tw.bjn.pg.event.handlers.join;

import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;

@Slf4j
@LineEventHandler("join")
public class JoinEventHandler extends EventHandler<JoinEvent> {

    @Autowired
    public JoinEventHandler(LineBotUtils lineBotUtils) {
        super(lineBotUtils);
    }

    @Override
    protected Message onEvent(JoinEvent event) {
        return new TextMessage("Hi, there");
    }
}
