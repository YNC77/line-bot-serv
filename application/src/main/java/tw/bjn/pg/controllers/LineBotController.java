package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.Event;

public abstract class LineBotController {
    protected abstract void processEvent(Event event);
}
