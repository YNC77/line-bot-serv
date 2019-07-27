package tw.bjn.pg.flows;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.interfaces.event.HandlerContainer;
import tw.bjn.pg.interfaces.flows.Flow;
import tw.bjn.pg.utils.LineBotUtils;

import java.util.Optional;

@Slf4j
@Component
public class ProcessEventFlow implements Flow {

    protected LineBotUtils lineBotUtils;
    protected HandlerContainer handlerContainer;

    @Autowired
    ProcessEventFlow(HandlerContainer handlerContainer, LineBotUtils lineBotUtils) {
        this.handlerContainer = handlerContainer;
        this.lineBotUtils = lineBotUtils;
    }

    @Override
    public void start(Event event) {
        try {
            EventHandler<Event> handler = handlerContainer.findSuitableHandler(event);
            log.debug("run handler - ({})", handler);
            Optional<Message> optionalMessage = handler.handle(event);
            if (optionalMessage.isPresent()) {
                final Message message = optionalMessage.get();
                lineBotUtils.pushMessage(event.getSource().getUserId(), message);
            }
        } catch (Exception e) {
            log.error("error occurs while handling event", e);
        }
    }
}
