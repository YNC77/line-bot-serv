package tw.bjn.pg.event.queue;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Preconditions;
import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventQueueManager;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * Temporary implementation,
 * will use rabbitmq solution later and separate handlers to another worker process
 */
@Slf4j
@Component
public class QueueManagerImpl implements EventQueueManager<Event>{

    @Value("${event.handler.queue.capacity}")
    private int QUEUE_CAPACITY;

    @Value("${event.handler.thread.count}")
    private int THREAD_COUNT;

    private LinkedBlockingQueue<Event> queue;

    private ExecutorService executorService;

    private List<Consumer<Event>> listeners;


    @PostConstruct
    public void init() {
        queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        listeners = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.submit(this::listening);
        log.info("init thread pool ({}) and queue ({})", THREAD_COUNT, QUEUE_CAPACITY);
    }

    @Override
    public boolean accept(Event event) {
        JsonTypeName type = event.getClass().getAnnotation(JsonTypeName.class);
        Preconditions.checkNotNull(type, "cannot get type of event"); // should not be null, unless something wrong with line sdk
        log.debug("Accept event ({}) with type ({})", event, type);
        return queue.offer(event);
    }

    private void listening() {
        while(true) {
            try {
                if (listeners.isEmpty()) {
                    // probably configuration error
                    Thread.sleep(1000);
                    continue;
                }
                Event event = queue.take();
                listeners.forEach(
                        listener -> executorService.submit(() -> {
                            try {
                                listener.accept(event);
                            } catch (Exception e) {
                                log.error("Exception in handler.", e);
                            }
                        }));
            } catch (InterruptedException e) {
                log.error("Interrupted.", e);
                break;
            } catch (Exception e) {
                log.error("Unexpected error.", e);
            }
        }
    }

    @Override
    public boolean registerListener(Consumer<Event> consumer) {
        return listeners.add(consumer);
    }
}
