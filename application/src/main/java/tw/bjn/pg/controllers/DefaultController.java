package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.flows.EnqueueFlow;
import tw.bjn.pg.interfaces.controllers.EnqueueController;

/**
 * Handle fallback event
 */
@LineMessageHandler
public class DefaultController extends EnqueueController {

    @Autowired
    public DefaultController(EnqueueFlow flow) {
        super(flow);
    }

    @EventMapping
    public void handleDefaultEvent(Event event) {
        processEvent(event);
    }
}
