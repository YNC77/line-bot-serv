package tw.bjn.pg.interfaces.event;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import tw.bjn.pg.utils.LineBotUtils;

import java.util.Optional;

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

    public Optional<Message> handle(Event event) throws ClassCastException {
        return Optional.ofNullable( onEvent((T) event) );
    }

    protected abstract Message onEvent(T event);
}
