package tw.bjn.pg.interfaces;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

// TODO: this must be refactor
@Slf4j
public abstract class EventHandler<T extends Event> {

    protected LineMessagingClient lineMessagingClient;

    public EventHandler (LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

    public abstract void handle(T e);
    public boolean pushMessage (PushMessage message) {
        try {
            CompletableFuture<BotApiResponse> future = lineMessagingClient.pushMessage( message );
            future.join();
        } catch (CancellationException | CompletionException e) {
            log.error("Exception occur when pushing message.", e);
            return false;
        }
        return true;
    }
}
