package tw.bjn.pg.interfaces.controllers;

import com.linecorp.bot.model.event.Event;

public abstract class LineBotController {
    protected abstract void processEvent(Event event);
}
