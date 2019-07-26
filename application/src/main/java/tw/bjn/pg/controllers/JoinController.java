package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
public class JoinController extends ReplyController {
    @EventMapping
    public Message handleJoinEvent(JoinEvent event) {
        log.info("event: {}", event);
        return new TextMessage("holo");
    }
}
