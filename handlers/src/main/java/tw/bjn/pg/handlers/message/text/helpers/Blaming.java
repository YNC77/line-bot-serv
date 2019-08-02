package tw.bjn.pg.handlers.message.text.helpers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.utils.MsgUtils;
import tw.bjn.pg.utils.YamlReader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class Blaming implements Skill {

    @Autowired
    MsgUtils msgUtils;
    private List<String> quotations = Collections.emptyList();

    @PostConstruct
    public void init() {
        quotations = getQuotations();
    }

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        return true;
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        return msgUtils.createTextMsg(getReplySentence());
    }
    @Override
    public int priority() {
        return 400;
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
