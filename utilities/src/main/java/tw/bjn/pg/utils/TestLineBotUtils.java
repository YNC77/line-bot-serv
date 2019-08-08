package tw.bjn.pg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Profile("test")
@Component
public class TestLineBotUtils implements LineBotUtils {
    @Override
    public boolean pushMessage(String userId, Message message) {
        log.debug("get userId '{}' with message '{}'", userId, message);
        return false;
    }

    @Override
    public CompletableFuture<BotApiResponse> replyMessage(String replyToken, Message message) {
        log.debug("get reply token '{}' with message '{}'", replyToken, message);
        try {
            ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
            log.debug(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("translate json failed.", e);
        }
        return null;
    }

    @Override
    public CompletableFuture<BotApiResponse> replyMessage(ReplyMessage replyMessage) {
        log.debug("get message '{}'", replyMessage);
        ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
        for (Message m : replyMessage.getMessages()) {
            try {
                log.debug(objectMapper.writeValueAsString(m));
            } catch (JsonProcessingException e) {
                log.error("translate json failed.", e);
            }
        }
        return null;
    }

    @Override
    public UserProfileResponse getUserProfile(String userId) {
        log.debug("get user id '{}'",userId);
        return new UserProfileResponse("Name", userId, "", "my status");
    }
}
