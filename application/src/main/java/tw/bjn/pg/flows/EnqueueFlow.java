package tw.bjn.pg.flows;

import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventDispatcher;
import tw.bjn.pg.interfaces.flows.Flow;

@Slf4j
@Component
public class EnqueueFlow implements Flow {

    protected EventDispatcher eventDispatcher;

    @Autowired
    EnqueueFlow(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void start(Event event) {
        eventDispatcher.accept(event);
    }
}
