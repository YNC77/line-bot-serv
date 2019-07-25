package tw.bjn.pg.interfaces;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;

public interface MessageHandler<T extends Event> {
    Message handle(T e);
}
