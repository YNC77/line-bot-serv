package tw.bjn.pg.interfaces.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import tw.bjn.pg.interfaces.event.EventDispatcher;

@Slf4j
public abstract class EnqueueController extends LineBotController {
    // TODO: using message queues to handle various messages, only reply urgent message immediately
    private EventDispatcher eventDispatcher;
    public EnqueueController(LineMessagingClient lineMessagingClient, EventDispatcher eventDispatcher){
        super(lineMessagingClient);
        this.eventDispatcher = eventDispatcher;
    }
    protected <T extends Event> void queue(T event) {
        if (!eventDispatcher.accept( event ))
            log.error("failed to enqueue event - {}", event);
    }

}
