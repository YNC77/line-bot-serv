package tw.bjn.pg.interfaces.controllers;

import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import tw.bjn.pg.flows.EnqueueFlow;
import tw.bjn.pg.interfaces.flows.Flow;

@Slf4j
public abstract class EnqueueController extends LineBotController {
    // TODO: using message queues to handle various messages, only reply urgent message immediately

    protected Flow enqueueFlow;

    public EnqueueController(EnqueueFlow enqueueFlow){
        this.enqueueFlow = enqueueFlow;
    }

    @Override
    protected void processEvent(Event event) {
        enqueueFlow.start(event);
    }


}
