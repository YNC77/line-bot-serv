package tw.bjn.pg.handlers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.EventHandler;

import java.util.concurrent.CompletableFuture;

/**
 * When someone follows your channel
 */
@Slf4j
@Component
public class FollowEventHandler extends EventHandler<FollowEvent> {

    @Autowired
    public FollowEventHandler(LineMessagingClient lineMessagingClient) {
        super(lineMessagingClient);
    }

    @Override
    public void handle(FollowEvent event) {
        try {
            CompletableFuture<UserProfileResponse> profileFuture = lineMessagingClient.getProfile( event.getSource().getUserId() );
            UserProfileResponse profile = profileFuture.join();
            log.info("get profile - {}", profile);
            pushMessage( new PushMessage(event.getSource().getUserId(), new TextMessage("Hi again, "+profile.getDisplayName())) );
        } catch (Exception ex) {
            log.error("error when communicate with line server", ex);
        }
    }
}
