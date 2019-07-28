package tw.bjn.pg.interfaces.flows;

import com.linecorp.bot.model.event.Event;

public interface Flow {
    void start(Event event);
}
