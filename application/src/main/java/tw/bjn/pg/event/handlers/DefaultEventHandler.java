package tw.bjn.pg.event.handlers;

import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;

@Slf4j
@LineEventHandler("default")
public class DefaultEventHandler extends EventHandler<Event> {

    @Autowired
    public DefaultEventHandler(LineBotUtils lineBotUtils) {
        super(lineBotUtils);
    }

    @Override
    public void onEvent(Event e) {
        log.info("get event - " + e);
    }
}