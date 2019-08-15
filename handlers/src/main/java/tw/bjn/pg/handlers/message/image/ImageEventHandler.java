package tw.bjn.pg.handlers.message.image;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.handlers.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;
import tw.bjn.pg.utils.MsgUtils;

@Slf4j
@LineEventHandler
public class ImageEventHandler extends EventHandler<MessageEvent<ImageMessageContent>> {

    @Autowired
    LineBotUtils lineBotUtils;

    @Autowired
    MsgUtils msgUtils;

    @Override
    protected Message onEvent(MessageEvent<ImageMessageContent> event) {
        log.info("image event: {}", event);
        lineBotUtils.getMessageContent(event.getMessage().getId());
        return msgUtils.createTextMsg("image saved");
    }
}
