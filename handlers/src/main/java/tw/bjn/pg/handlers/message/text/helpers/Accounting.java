package tw.bjn.pg.handlers.message.text.helpers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.postgres.Database;
import tw.bjn.pg.postgres.model.ExpenseRecord;
import tw.bjn.pg.utils.MsgUtils;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Accounting implements Skill {

    @Autowired
    Database database;

    @Autowired
    MsgUtils msgUtils;

    private Pattern p = Pattern.compile("^\\$([0-9]+|\\?)");

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        final String message = event.getMessage().getText();
        String trimmed = message.trim();
        return p.matcher(trimmed).matches();
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        log.info("event: {}", event.getMessage().getText().substring(1));

        String numeric = event.getMessage().getText().trim().substring(1);
        String uid = event.getSource().getUserId();

        if ("?".equals(numeric)) {
            int total = database.queryTotalPrice(uid);
            return msgUtils.createTextMsg("sum: " + total);
        }

        if ("history".equals(numeric)) {
            List<ExpenseRecord> expenseRecords = database.queryByUid(uid);
            StringBuilder builder = new StringBuilder();
            expenseRecords.stream()
                    .sorted(Comparator.comparing(ExpenseRecord::getTime))
                    .forEach(builder::append);
            return msgUtils.createTextMsg(builder.toString());
        }

        int price = Integer.parseInt(numeric);
        boolean insertResult = database.insert(uid, price, Timestamp.from(event.getTimestamp()));
        // TODO: error handling
        if (!insertResult)
            return null;
        return msgUtils.createTextMsg(event.getMessage().getText());
    }

    @Override
    public int priority() {
        return 50;
    }
}
