package tw.bjn.pg.event.queue;

import com.linecorp.bot.model.event.Event;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface EventQueueManager<T extends Event> {
    boolean accept(T event);
    T poll(long timeout, TimeUnit unit);
    // TODO: support multi-queue
}
