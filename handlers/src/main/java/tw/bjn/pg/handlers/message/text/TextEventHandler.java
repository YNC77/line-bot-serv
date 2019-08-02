package tw.bjn.pg.handlers.message.text;

import com.google.common.collect.Lists;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;

import tw.bjn.pg.handlers.EventHandler;
import tw.bjn.pg.handlers.message.text.helpers.Accounting;
import tw.bjn.pg.handlers.message.text.helpers.Blaming;
import tw.bjn.pg.handlers.message.text.helpers.Calculating;
import tw.bjn.pg.handlers.message.text.helpers.Skill;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@LineEventHandler
public class TextEventHandler extends EventHandler<MessageEvent<TextMessageContent>> {

    private List<Skill> skills;

    @Autowired
    Calculating calculating;

    @Autowired
    Accounting accounting;

    @Autowired
    Blaming blaming;

    @PostConstruct
    public void init() {
        skills = Lists.newArrayList(calculating, accounting, blaming);
    }

    @Override
    public Message onEvent(MessageEvent<TextMessageContent> event) {
        List<Skill> skillList = skills.stream()
                .filter(skill -> skill.isCapable(event))
                .sorted(Comparator.comparingInt(Skill::priority))
                .collect(Collectors.toList());

        for (Skill skill : skillList) {
            try {
                Message message = skill.perform(event);
                if (message != null) // handled
                    return message;
            } catch (Exception e) {
                log.error("error when try to perform", e);
            }
        }
        log.warn("cannot find suitable skill for event ({})", event);
        return null;
    }
}
