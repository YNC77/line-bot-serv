package tw.bjn.pg.utils;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import java.util.concurrent.CompletableFuture;

public interface LineBotUtils {
    boolean pushMessage(String userId, Message message);
    CompletableFuture<BotApiResponse> replyMessage(ReplyMessage replyMessage);
    CompletableFuture<BotApiResponse> replyMessage(String replyToken, Message message);
    UserProfileResponse getUserProfile(String userId);
}
