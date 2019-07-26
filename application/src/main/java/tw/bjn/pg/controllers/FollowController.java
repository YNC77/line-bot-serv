package tw.bjn.pg.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.interfaces.controllers.EnqueueController;
import tw.bjn.pg.interfaces.event.EventDispatcher;

@Slf4j
@LineMessageHandler
public class FollowController extends EnqueueController {

    @Autowired
    public FollowController(LineMessagingClient lineMessagingClient,
                            EventDispatcher eventDispatcher) {
        super(lineMessagingClient, eventDispatcher);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        log.info("event: {}", event);
        queue(event);
    }

    @EventMapping
    public void handleUnFollowEvent(UnfollowEvent event) {
        log.info("event: {}", event);
//        return new TextMessage("bye");
//  cannot return, it's not a reply event...
// it will fail when sdk try to convert original event to ReplyEvent
    }

}
