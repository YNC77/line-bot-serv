package tw.bjn.pg.interfaces.event;

import com.linecorp.bot.model.event.Event;

import java.util.Map;

public interface HandlerContainer {
    EventHandler<Event> findSuitableHandler(Event event);
    Map<String, EventHandler<Event>> getHandlers();
}
