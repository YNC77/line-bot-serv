package tw.bjn.pg.interfaces.event;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Base event handler class. onEvent function will be called by working thread.
 * @param <T> Handled event type
 */
@Slf4j
public abstract class EventHandler<T extends Event> {

    public Optional<ReplyMessage> handle(Event event) throws ClassCastException {
        Message message = onEvent((T) event);
        if (message == null)
            return Optional.empty();
        if (!(event instanceof ReplyEvent)) {
            log.warn("Handler ({}) - Don't return message if you are not a ReplyEvent, use push message directly instead.", this);
            return Optional.empty();
        }
        return Optional.of(new ReplyMessage(((ReplyEvent) event).getReplyToken(), message));
    }

    protected abstract Message onEvent(T event);
}
