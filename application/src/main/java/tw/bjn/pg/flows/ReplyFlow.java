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
import tw.bjn.pg.interfaces.event.EventDispatcher;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.interfaces.event.HandlerContainer;
import tw.bjn.pg.interfaces.flows.Flow;
import tw.bjn.pg.utils.LineBotUtils;

import java.util.Optional;

/**
 * flow for process ReplyEvent.
 * start from pickup handler, process event, reply message.
 */
@Slf4j
@Component
public class ReplyFlow implements Flow {

    protected HandlerContainer handlerContainer;
    protected LineBotUtils lineBotUtils;

    @Autowired
    public ReplyFlow(HandlerContainer handlerContainer, LineBotUtils lineBotUtils) {
        this.handlerContainer = handlerContainer;
        this.lineBotUtils = lineBotUtils;
    }

    @Override
    public void start(Event event) {
        Preconditions.checkArgument( event instanceof ReplyEvent, "Cannot handle non-reply event" );
        EventHandler<Event> handler = handlerContainer.findSuitableHandler(event);
        Optional<Message> optionalMessage = handler.handle(event);
        optionalMessage.ifPresent(message ->
                reply( ((ReplyEvent)event).getReplyToken(), message)
        );
    }

    protected void reply(String replyToken, Message message) {
        lineBotUtils.replyMessage(replyToken, message).whenComplete(this::onReplyDone);
    }

    protected void onReplyDone(BotApiResponse botApiResponse, Throwable throwable) {
        if( throwable != null )
            log.error("reply failed.", throwable);
        else
            log.info("response: {}", botApiResponse);
    }
}
