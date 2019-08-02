package tw.bjn.pg.handlers.follow;

import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.handlers.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;

/**
 * When someone follows your channel
 */
@Slf4j
@LineEventHandler
public class FollowEventHandler extends EventHandler<FollowEvent> {

    protected LineBotUtils lineBotUtils;

    @Autowired
    public FollowEventHandler(LineBotUtils lineBotUtils) {
        this.lineBotUtils = lineBotUtils;
    }

    public Message onEvent(FollowEvent event) {
        UserProfileResponse profile = lineBotUtils.getUserProfile(event.getSource().getUserId());
        return new TextMessage("Hi there, "+profile.getDisplayName());
    }
}
