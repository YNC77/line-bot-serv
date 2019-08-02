package tw.bjn.pg.flows;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.handlers.EventHandler;
import tw.bjn.pg.container.HandlerContainer;
import tw.bjn.pg.utils.LineBotUtils;

import java.util.Optional;

@Slf4j
@Component
public class DispatchAndProcessEventFlow implements Flow {

    protected LineBotUtils lineBotUtils;
    protected HandlerContainer handlerContainer;

    @Autowired
    DispatchAndProcessEventFlow(HandlerContainer handlerContainer, LineBotUtils lineBotUtils) {
        this.handlerContainer = handlerContainer;
        this.lineBotUtils = lineBotUtils;
    }

    @Override
    public void start(Event event) {
        try {
            EventHandler<Event> handler = handlerContainer.findSuitableHandler(event);
            if (handler == null) {
                log.error("No available handler to handle event ({})", event);
                return;
            }
            log.debug("process ({}) event with ({}) handler", event, handler);
            Optional<ReplyMessage> optionalReply = handler.handle(event);
            optionalReply.ifPresent(lineBotUtils::replyMessage);
        } catch (Exception e) {
            log.error("error occurs while handling event", e);
            // currently, just drop this message...
        }
    }
}
