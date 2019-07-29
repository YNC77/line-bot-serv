package tw.bjn.pg.interfaces.event;

import com.linecorp.bot.model.event.Event;

import java.util.function.Consumer;

public interface EventQueueManager<T extends Event> {
    boolean accept(T event);
    boolean registerListener(Consumer<T> consumer);
    // TODO: support multi-queue
}
