package tw.bjn.pg.container;

import com.linecorp.bot.model.event.Event;
import tw.bjn.pg.handlers.EventHandler;

import java.util.Collection;

public interface HandlerContainer {
    EventHandler<Event> findSuitableHandler(Event event);
    Collection<EventHandler<Event>> getHandlers();
}
