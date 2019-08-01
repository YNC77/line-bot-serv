package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.dao.TestDatabase;

import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.MsgUtils;
import tw.bjn.pg.utils.YamlReader;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component

public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    private MsgUtils msgUtils;
    private TestDatabase testDatabase;

    @Autowired
    TextEventHandler(MsgUtils msgUtils, TestDatabase testDatabase) {
        this.msgUtils = msgUtils;
        this.testDatabase = testDatabase;
    }

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        if (event.getMessage().getText().contains("$")) {
            log.info("event: {}", event.getMessage().getText().substring(1));
            int getPrice = Integer.parseInt(event.getMessage().getText().substring(1));
            boolean insertResult = testDatabase.insert(event.getSource().getUserId(), getPrice, Timestamp.from(event.getTimestamp()));

            return msgUtils.createTextMsg(event.getMessage().getText());
        } else {
            return msgUtils.createTextMsg(getReplySentence());
        }

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
