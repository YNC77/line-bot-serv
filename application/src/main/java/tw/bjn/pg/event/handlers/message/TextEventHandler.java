package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.MsgUtils;
import tw.bjn.pg.utils.YamlReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    private MsgUtils msgUtils;

    @Autowired
    TextEventHandler(MsgUtils msgUtils) {
        this.msgUtils = msgUtils;
    }

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        return msgUtils.createTextMsg(getReplySentence());
    }

    public Map<String, List<String>> getQuotations() {
        Map<String, List<String>> mQuotations = null;
        try{
            YamlReader<Map<String, List<String>>> reader = new YamlReader<>();
            mQuotations = reader.parse("quotations.yml");
        } catch (IOException e) {
//            log.error("幹",e);
        }

        return mQuotations;
    }

    public String getReplySentence() {
        Map<String, List<String>> mQuotations = getQuotations();
        Random random = new Random();
        int replyIndex = random.nextInt(5);

        return mQuotations.get("replySentences").get(replyIndex);
    }
}
