package tw.bjn.pg.interfaces;

import com.linecorp.bot.model.event.Event;

public interface EventDispatcher {
    boolean accept(Event event);
}
