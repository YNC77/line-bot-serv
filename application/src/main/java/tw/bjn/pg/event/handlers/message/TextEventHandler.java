package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.calculator.Calculator;

import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.postgres.Database;
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
    private Database testDatabase; // TODO: use repository directly
    private Calculator calculator;
    private List<String> quotations = Collections.emptyList();

    @Autowired
    TextEventHandler(MsgUtils msgUtils, Database testDatabase, Calculator calculator) {
        this.msgUtils = msgUtils;
        this.testDatabase = testDatabase;
        this.calculator = calculator;
    }

    @PostConstruct
    public void init() {
        quotations = getQuotations();
    }

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        final String message = event.getMessage().getText();
        if (message.matches("[0-9+\\-*/()\\s]+")) {
            try {
                int result = calculator.calc(message);
                return msgUtils.createTextMsg(String.valueOf(result));
            } catch (Exception e) {
                log.error("failed to calculate", e);
            }
        }
        if (message.contains("$")) { // TODO: not beautiful
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
