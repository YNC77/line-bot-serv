package tw.bjn.pg.interfaces.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;
import tw.bjn.pg.interfaces.event.EventDispatcher;

public abstract class LineBotController {

    protected LineMessagingClient lineMessagingClient;
    protected EventDispatcher eventDispatcher;

    protected abstract void processEvent(Event event);
}
