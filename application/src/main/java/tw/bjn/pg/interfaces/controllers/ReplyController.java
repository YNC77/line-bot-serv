package tw.bjn.pg.interfaces.controllers;

import com.google.common.base.Preconditions;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.flows.ReplyFlow;
import tw.bjn.pg.interfaces.event.EventDispatcher;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.interfaces.flows.Flow;

import java.util.Optional;

@Slf4j
public abstract class ReplyController extends LineBotController { // refactor
    // reply token:  message, follow, join, postback, beancon
    protected Flow replyFlow;

    protected ReplyController(ReplyFlow replyFlow) {
        this.replyFlow = replyFlow;
    }

    @Override
    protected void processEvent(Event event) {
        log.info("event: {}", event);
        replyFlow.start(event);
    }
}
