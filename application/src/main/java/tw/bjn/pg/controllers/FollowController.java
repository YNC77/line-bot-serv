package tw.bjn.pg.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.flows.EnqueueFlow;
import tw.bjn.pg.interfaces.controllers.EnqueueController;
import tw.bjn.pg.interfaces.event.EventDispatcher;

@Slf4j
@LineMessageHandler
public class FollowController extends EnqueueController {

    @Autowired
    public FollowController(EnqueueFlow flow) {
        super(flow);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        processEvent(event);
    }

    @EventMapping
    public void handleUnFollowEvent(UnfollowEvent event) {
        processEvent(event);
    }

}
