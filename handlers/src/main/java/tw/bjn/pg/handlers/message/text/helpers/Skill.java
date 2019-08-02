package tw.bjn.pg.handlers.message.text.helpers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

public interface Skill {
    boolean isCapable(MessageEvent<TextMessageContent> event);
    Message perform(MessageEvent<TextMessageContent> event);
    int priority(); // maybe we can use annotation instead
}
