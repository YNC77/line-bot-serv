package tw.bjn.pg.event.handlers.join;

import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;

@Slf4j
@LineEventHandler("join")
public class JoinEventHandler extends EventHandler<JoinEvent> {

    @Override
    protected Message onEvent(JoinEvent event) {
        return new TextMessage("Hi, there");
    }
}
