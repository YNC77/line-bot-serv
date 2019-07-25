package tw.bjn.pg.handlers;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Slf4j
@Component
public class EventDispatcher {

    private LinkedBlockingQueue<Event> queue;
    private ExecutorService executorService;

    @Value("${event.handler.thread.count}")
    private int THREAD_COUNT;

    @Value("${event.handler.queue.capacity}")
    private int QUEUE_CAPACITY;

    @PostConstruct
    public void init() {
        queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.submit(this::polling);
        log.error("init with queue capacity ({}) and thread count ({}) ", QUEUE_CAPACITY, THREAD_COUNT);
    }

    public boolean accept( MessageEvent<?> event ){
        return queue.offer(event);
    }

    private void polling() {
        while(true){
            Event messageEvent = queue.poll();
        }
    }
}
