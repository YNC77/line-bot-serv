package tw.bjn.pg.interfaces.event;

import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import tw.bjn.pg.utils.LineBotUtils;

/**
 * Base event handler class. onEvent function will be called by working thread.
 * @param <T> Handled event type
 */
@Slf4j
public abstract class EventHandler<T extends Event> {

    protected LineBotUtils lineBotUtils;
    public EventHandler(LineBotUtils lineBotUtils) {
        this.lineBotUtils = lineBotUtils;
    }

    public void handle(Event event) throws ClassCastException {
        onEvent((T) event);
    }

    public abstract void onEvent(T o);
}
