package tw.bjn.pg.interfaces.event;

import com.linecorp.bot.model.event.Event;

public interface EventDispatcher {
    void onMessage(Event event);
}
