package tw.bjn.pg.event.dispatcher;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Preconditions;
import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventDispatcher;
import tw.bjn.pg.interfaces.event.EventHandler;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
public class EventDispatcherImpl implements EventDispatcher {

    private LinkedBlockingQueue<Event> queue; // TODO: make this virtual
    private ExecutorService executorService;
    private Map<String, EventHandler> handlers;

    @Value("${event.handler.thread.count}")
    private int THREAD_COUNT;

    @Value("${event.handler.queue.capacity}")
    private int QUEUE_CAPACITY;

    final private ApplicationContext applicationContext;

    @Autowired
    public EventDispatcherImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        handlers = new HashMap<>();
        Map<String,Object> mapHandlers = applicationContext.getBeansWithAnnotation(LineEventHandler.class);
        mapHandlers.forEach((beanName, beanObj)->{
            LineEventHandler type = beanObj.getClass().getAnnotation(LineEventHandler.class);
            handlers.put(type.value(), (EventHandler) beanObj);
            log.info("Add ({}) event handler.", type);
        });
        queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.submit(this::polling);
        log.debug("init with queue capacity ({}) and thread count ({}) ", QUEUE_CAPACITY, THREAD_COUNT);
    }

    public <T extends Event> boolean accept(T event) {
        JsonTypeName type = event.getClass().getAnnotation(JsonTypeName.class);
        Preconditions.checkNotNull(type); // should not be null, unless something wrong with line sdk
        log.debug("Accept event ({}) with type ({})", event, type);
        return queue.offer(event);
    }

    private void polling() {
        while(true){
            try {
                Event messageEvent = queue.take();
                log.info("Get event - {}", messageEvent);
                JsonTypeName type = messageEvent.getClass().getAnnotation(JsonTypeName.class);
                EventHandler handler = findSuitableHandler(type);
                log.debug("get handler - ({}) ({})", handler, type);
                executorService.submit(() -> {
                    try {
                        handler.handle(messageEvent);
                    } catch (Exception e) {
                        log.error("error occurs while handling event", e);
                    }
                });
            } catch ( RejectedExecutionException e ) {
                log.error("cannot submit task", e);
            } catch ( InterruptedException e ){
                log.info("get interrupted", e);
                break;
            }
        }
    }

    private EventHandler findSuitableHandler(JsonTypeName type) {
        // TODO: need a smarter way to classify various event
        if( type == null || !handlers.containsKey(type.value()) )
            return handlers.get("unknown");
        return handlers.get(type.value());
    }
}
