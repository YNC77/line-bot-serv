package tw.bjn.pg.flows;

import com.google.common.base.Preconditions;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.interfaces.event.HandlerContainer;
import tw.bjn.pg.interfaces.flows.Flow;
import tw.bjn.pg.interfaces.utils.ILineBotUtils;

import java.util.Optional;

/**
 * flow for process ReplyEvent.
 * start from pickup handler, process event, reply message.
 */
@Deprecated
@Slf4j
@Component
public class ReplyFlow implements Flow {

    protected HandlerContainer handlerContainer;
    protected ILineBotUtils lineBotUtils;

    @Autowired
    public ReplyFlow(HandlerContainer handlerContainer, ILineBotUtils lineBotUtils) {
        this.handlerContainer = handlerContainer;
        this.lineBotUtils = lineBotUtils;
    }

    @Override
    public void start(Event event) {
        Preconditions.checkArgument( event instanceof ReplyEvent, "Cannot handle non-reply event" );
        EventHandler<Event> handler = handlerContainer.findSuitableHandler(event);
        Optional<ReplyMessage> optionalMessage = handler.handle(event);
        optionalMessage.ifPresent(this::reply);
    }

    protected void reply(String replyToken, Message message) {
        reply(new ReplyMessage(replyToken, message));
    }
    protected void reply(ReplyMessage message) {
        lineBotUtils.replyMessage(message).whenComplete(this::onReplyDone);
    }

    protected void onReplyDone(BotApiResponse botApiResponse, Throwable throwable) {
        if( throwable != null )
            log.error("reply failed.", throwable);
        else
            log.info("response: {}", botApiResponse);
    }
}
