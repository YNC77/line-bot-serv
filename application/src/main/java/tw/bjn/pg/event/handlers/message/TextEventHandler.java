package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.dao.Database;

import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.utils.MsgUtils;
import tw.bjn.pg.utils.YamlReader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@LineEventHandler
public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    private MsgUtils msgUtils;
    private Database testDatabase;
    private List<String> quotations = Collections.emptyList();

    @Autowired
    TextEventHandler(MsgUtils msgUtils, Database testDatabase) {
        this.msgUtils = msgUtils;
        this.testDatabase = testDatabase;
    }

    @PostConstruct
    public void init() {
        quotations = getQuotations();
    }

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        if (event.getMessage().getText().contains("$")) { // TODO: not beautiful
            log.info("event: {}", event.getMessage().getText().substring(1));
            int getPrice = Integer.parseInt(event.getMessage().getText().substring(1));
            boolean insertResult = testDatabase.insert(event.getSource().getUserId(), getPrice, Timestamp.from(event.getTimestamp()));
            // TODO: error handling

            return msgUtils.createTextMsg(event.getMessage().getText());
        } else {
            return msgUtils.createTextMsg(getReplySentence());
        }

    }

    public List<String> getQuotations() {
        List<String> quotationlist = Collections.emptyList();
        try{
            YamlReader<List<String>> reader = new YamlReader<>();
            quotationlist = reader.parse("quotations.yml");
        } catch (IOException e) {
            log.error("Read yaml failed.", e);
        }

        return quotationlist;
    }

    public String getReplySentence() {
        if (quotations.isEmpty())
            return "";

        Random random = new Random();
        int replyIndex = random.nextInt(quotations.size());

        return quotations.get(replyIndex);
    }
}
