package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.flows.EnqueueFlow;

@Slf4j
@LineMessageHandler
public class JoinController extends EnqueueController {

    @Autowired
    public JoinController(EnqueueFlow flow) {
        super(flow);
    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        processEvent(event);
    }
}
