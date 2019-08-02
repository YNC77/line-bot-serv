package tw.bjn.pg.handlers.fallback;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.handlers.EventHandler;

@Slf4j
@LineEventHandler(fallback = true)
public class DefaultEventHandler extends EventHandler<Event> {

    @Override
    protected Message onEvent(Event e) {
        log.info("get event - " + e);
        return null;
    }
}