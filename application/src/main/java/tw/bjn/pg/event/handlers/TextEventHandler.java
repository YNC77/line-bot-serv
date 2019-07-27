package tw.bjn.pg.event.handlers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
//import tw.bjn.pg.annotations.LineEventHandler;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.LineBotUtils;
import tw.bjn.pg.utils.MsgUtils;
import tw.bjn.pg.utils.YamlReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
//@LineEventHandler("message")
public class TextEventHandler {

    @Autowired
    MsgUtils msgUtils;

    public TextEventHandler(LineBotUtils lineBotUtils) {
//        super(lineBotUtils);
    }

//    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        return msgUtils.createTextMsg(getReplySentence());
    }

    public Map<String, List<String>> getQuotations() {
        Map<String, List<String>> mQuotations = null;
        try{
            YamlReader<Map<String, List<String>>> reader = new YamlReader<>();
            mQuotations = reader.parse("quotations.yml");
        } catch (IOException e) {
            log.error("幹",e);
        }

        return mQuotations;
    }

    public String getReplySentence() {
        Map<String, List<String>> mQuotations = getQuotations();
        Random random = new Random();
        int replyIndex = random.nextInt(3);

        return mQuotations.get("replySentences").get(replyIndex);
    }
}
