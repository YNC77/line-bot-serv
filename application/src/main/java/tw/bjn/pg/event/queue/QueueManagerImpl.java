package tw.bjn.pg.event.queue;

import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventQueueManager;
import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Temporary implementation,
 * will use rabbitmq solution later and separate handlers to another worker process
 * ## forget about this plan, this will cost lots of "dyno's hours" and we're not gonna pay that.
 */
@Slf4j
@Component
public class QueueManagerImpl implements EventQueueManager<Event>{

    @Value("${event.handler.queue.capacity}")
    private int QUEUE_CAPACITY;

    private LinkedBlockingQueue<Event> queue;

    @PostConstruct
    public void init() {
        queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        log.info("init queue size ({})", QUEUE_CAPACITY);
    }

    @Override
    public boolean accept(Event event) {
        return queue.offer(event);
    }

    @Override
    public Event poll(long timeout, TimeUnit unit) {
        try {
            if (timeout == -1)
                return queue.take();
            if (timeout == 0)
                return queue.poll();
            return queue.poll(timeout, unit);
        } catch (InterruptedException e) {
            log.error("Interrupted.", e);
            Thread.currentThread().interrupt(); // set interrupt flag
            return null;
        }
    }
}
