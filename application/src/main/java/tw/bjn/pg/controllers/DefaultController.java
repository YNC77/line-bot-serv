package tw.bjn.pg.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.interfaces.controllers.EnqueueController;
import tw.bjn.pg.interfaces.event.EventDispatcher;

@LineMessageHandler
public class DefaultController extends EnqueueController {

    @Autowired
    public DefaultController(LineMessagingClient lineMessagingClient,
                             EventDispatcher eventDispatcher) {
        super(lineMessagingClient, eventDispatcher);
    }

    @EventMapping
    public void handleDefaultEvent(Event event) {
        queue(event);
    }

}
