package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.interfaces.EventDispatcher;

@Slf4j
@LineMessageHandler
public class FollowController extends EnqueueController {

    @Autowired
    public FollowController(EventDispatcher eventDispatcher) {
        super(eventDispatcher);
    }

    @EventMapping
    public Message handleFollowEvent(FollowEvent event) {
        log.info("event: {}", event);
        assign(event);
        return new TextMessage("Hello");
    }

    @EventMapping
    public Message handleUnFollowEvent(UnfollowEvent event) {
        log.info("event: {}", event);
        return new TextMessage("bye");
    }

}
