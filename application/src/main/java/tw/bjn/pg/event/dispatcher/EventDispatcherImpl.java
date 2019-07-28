package tw.bjn.pg.event.dispatcher;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Preconditions;
import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tw.bjn.pg.flows.ProcessEventFlow;
import tw.bjn.pg.interfaces.event.EventDispatcher;
import tw.bjn.pg.interfaces.flows.Flow;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Slf4j
@Component
public class EventDispatcherImpl implements EventDispatcher {

    private LinkedBlockingQueue<Event> queue; // TODO: make this virtual
    private ExecutorService executorService;

    @Value("${event.handler.thread.count}")
    private int THREAD_COUNT;

    @Value("${event.handler.queue.capacity}")
    private int QUEUE_CAPACITY;

    protected Flow processEventFlow;

    @Autowired
    public EventDispatcherImpl(ProcessEventFlow flow) {
        processEventFlow = flow;
    }

    @PostConstruct
    public void init() {
        queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.submit(this::polling);
        log.debug("init with queue capacity ({}) and thread count ({}) ", QUEUE_CAPACITY, THREAD_COUNT);
    }

    public <T extends Event> boolean accept(T event) {
        JsonTypeName type = event.getClass().getAnnotation(JsonTypeName.class);
        Preconditions.checkNotNull(type, "cannot get type of event"); // should not be null, unless something wrong with line sdk
        log.debug("Accept event ({}) with type ({})", event, type);
        return queue.offer(event);
    }

    private void polling() {
        while(true){
            try {
                Event messageEvent = queue.take();
                log.info("Get event - {}", messageEvent);
                executorService.submit(() ->
                    processEventFlow.start(messageEvent)
                );
            } catch ( RejectedExecutionException e ) {
                log.error("cannot submit task", e);
            } catch ( InterruptedException e ){
                log.info("get interrupted", e);
                break;
            }
        }
    }
}
