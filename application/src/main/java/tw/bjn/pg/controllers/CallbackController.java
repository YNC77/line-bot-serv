package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@LineMessageHandler
public class CallbackController {

    // TODO: using message queues to handle various messages, only reply urgent message immediately

    // reply token:  message, follow, join, postback, beancon

    @PostConstruct
    public void init () {
        log.error("controller constructed");
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        log.info("event: {}", event);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: {}", event);
        return new TextMessage(event.getMessage().getText());
    }
}
