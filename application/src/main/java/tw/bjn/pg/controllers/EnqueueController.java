package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.Event;
import tw.bjn.pg.interfaces.EventDispatcher;

public abstract class EnqueueController extends ReplyController {
    // TODO: using message queues to handle various messages, only reply urgent message immediately
    private EventDispatcher eventDispatcher;
    EnqueueController(EventDispatcher eventDispatcher){
        this.eventDispatcher = eventDispatcher;
    }
    protected <T extends Event> boolean assign(T event) {
        return eventDispatcher.accept( event );
    }

}
