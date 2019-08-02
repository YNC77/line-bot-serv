package tw.bjn.pg.controllers;

import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.flows.EnqueueFlow;

@Slf4j
@LineMessageHandler
public class PostbackController extends EnqueueController {

    @Autowired
    public PostbackController(EnqueueFlow flow) {
        super(flow);
    }
}
