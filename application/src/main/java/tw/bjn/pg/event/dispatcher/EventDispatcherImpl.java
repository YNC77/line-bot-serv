package tw.bjn.pg.event.dispatcher;

import com.linecorp.bot.model.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tw.bjn.pg.flows.DispatchAndProcessEventFlow;
import tw.bjn.pg.event.queue.EventQueueManager;
import tw.bjn.pg.flows.Flow;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *  TODO: multiple-queue, hadnle rabbitmq queue ( no plan )
 */
@Slf4j
@Component
public class EventDispatcherImpl implements EventDispatcher {

    protected Flow flow;
    protected EventQueueManager<Event> queueManager;


    @Value("${event.handler.thread.count}")
    private int THREAD_COUNT;

    private ExecutorService executorService;

    @Autowired
    public EventDispatcherImpl(EventQueueManager<Event> queueManager, DispatchAndProcessEventFlow flow) {
        this.flow = flow;
        this.queueManager = queueManager;
    }

    @PostConstruct
    public void init() {
        log.info("init thread pool with size ({})", THREAD_COUNT);
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.submit(this::listening);
    }

    private void listening() {
        log.info("start listening");
        while (!Thread.currentThread().isInterrupted()) {
            Event event = queueManager.poll(-1, TimeUnit.SECONDS);
            try {
                executorService.submit(() -> flow.start(event));
            } catch (RejectedExecutionException e) {
                log.error("cannot submit task.", e);
            } catch (Exception e) {
                log.error("Exception in handler.", e);
            }
        }
        log.info("end listening");
    }
}
