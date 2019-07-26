package tw.bjn.pg.interfaces.event;

import com.linecorp.bot.model.event.Event;

public interface EventDispatcher {
    <T extends Event> boolean accept(T event);
}
