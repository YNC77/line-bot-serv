package tw.bjn.pg.event.dispatcher;

import com.linecorp.bot.model.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.flows.DispatchAndProcessEventFlow;
import tw.bjn.pg.interfaces.event.EventDispatcher;
import tw.bjn.pg.interfaces.event.EventQueueManager;
import tw.bjn.pg.interfaces.flows.Flow;

import javax.annotation.PostConstruct;

/**
 *  TODO: multiple-queue, hadnle rabbitmq queue
 */
@Slf4j
@Component
public class EventDispatcherImpl implements EventDispatcher {

    protected Flow flow;
    protected EventQueueManager<Event> queueManager;

    @Autowired
    public EventDispatcherImpl(EventQueueManager<Event> queueManager, DispatchAndProcessEventFlow flow) {
        this.flow = flow;
        this.queueManager = queueManager;
    }

    @PostConstruct
    public void init() {
        queueManager.registerListener(this::onMessage);
    }

    public void onMessage(Event messageEvent) {
        log.info("Get event - {}", messageEvent);
        flow.start(messageEvent); // dispatch and process event
    }
}
