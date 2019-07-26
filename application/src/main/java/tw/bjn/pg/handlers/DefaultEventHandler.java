package tw.bjn.pg.handlers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.EventHandler;

@Slf4j
@LineEventHandler
public class DefaultEventHandler extends EventHandler<Event> {

    @Autowired
    public DefaultEventHandler(LineMessagingClient lineMessagingClient) {
        super(lineMessagingClient);
    }

    @Override
    public void handle(Event e) {
        log.info("get event - " + e);
    }
}