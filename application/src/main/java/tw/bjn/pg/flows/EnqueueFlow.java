package tw.bjn.pg.flows;

import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventQueueManager;
import tw.bjn.pg.interfaces.flows.Flow;

@Slf4j
@Component
public class EnqueueFlow implements Flow {

    protected EventQueueManager<Event> eventQueueManager;

    @Autowired
    EnqueueFlow(EventQueueManager<Event> eventQueueManager) {
        this.eventQueueManager = eventQueueManager;
    }

    @Override
    public void start(Event event) {
        eventQueueManager.accept(event);
    }
}
