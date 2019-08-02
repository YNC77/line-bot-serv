package tw.bjn.pg.flows;

import com.linecorp.bot.model.event.Event;

public interface Flow {
    void start(Event event);
}
