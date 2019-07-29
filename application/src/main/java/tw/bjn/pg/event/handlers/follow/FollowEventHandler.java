package tw.bjn.pg.event.handlers.follow;

import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.interfaces.utils.ILineBotUtils;

/**
 * When someone follows your channel
 */
@Slf4j
@LineEventHandler("follow")
public class FollowEventHandler extends EventHandler<FollowEvent> {

    protected ILineBotUtils lineBotUtils;

    @Autowired
    public FollowEventHandler(ILineBotUtils lineBotUtils) {
        this.lineBotUtils = lineBotUtils;
    }

    public Message onEvent(FollowEvent event) {
        UserProfileResponse profile = lineBotUtils.getUserProfile(event.getSource().getUserId());
        return new TextMessage("Hi there, "+profile.getDisplayName());
    }
}
