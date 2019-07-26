package tw.bjn.pg.interfaces.controllers;

import com.linecorp.bot.client.LineMessagingClient;

public abstract class LineBotController {

    protected LineMessagingClient lineMessagingClient;

    public LineBotController(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }
}
