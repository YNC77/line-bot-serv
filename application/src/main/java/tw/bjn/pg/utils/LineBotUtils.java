package tw.bjn.pg.utils;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.utils.ILineBotUtils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Profile("heroku")
@Component
public class LineBotUtils implements ILineBotUtils{

    private LineMessagingClient lineMessagingClient;

    @Autowired
    LineBotUtils(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

    public boolean pushMessage(String userId, Message message) {
        try {
            PushMessage pushMessage = new PushMessage(userId, message);
            CompletableFuture<BotApiResponse> future = lineMessagingClient.pushMessage(pushMessage);
            future.join();
        } catch (CancellationException | CompletionException e) {
            log.error("Exception occur when pushing message.", e);
            return false;
        }
        return true;
    }

    public CompletableFuture<BotApiResponse> replyMessage(String replyToken, Message message) {
        return replyMessage(new ReplyMessage(replyToken, message));
    }

    public CompletableFuture<BotApiResponse> replyMessage(ReplyMessage replyMessage) {
        return lineMessagingClient.replyMessage(replyMessage);
    }

    public UserProfileResponse getUserProfile(String userId) {
        try {
            CompletableFuture<UserProfileResponse> profileFuture = lineMessagingClient.getProfile( userId );
            UserProfileResponse profile = profileFuture.join();
            log.info("get profile - {}", profile);
            return profile;
        } catch (CancellationException | CompletionException e) {
            log.error("Exception occur when pushing message.", e);
            return null;
        }
    }
}
