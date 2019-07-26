package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import tw.bjn.pg.interfaces.EventDispatcher;

@Slf4j
public abstract class EnqueueController extends ReplyController {
    // TODO: using message queues to handle various messages, only reply urgent message immediately
    private EventDispatcher eventDispatcher;
    EnqueueController(EventDispatcher eventDispatcher){
        this.eventDispatcher = eventDispatcher;
    }
    protected <T extends Event> void assign(T event) {
        if (!eventDispatcher.accept( event ))
            log.error("failed to enqueue event - {}", event);
    }

}
