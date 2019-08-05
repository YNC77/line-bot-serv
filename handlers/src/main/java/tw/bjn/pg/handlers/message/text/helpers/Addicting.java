package tw.bjn.pg.handlers.message.text.helpers;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.ptt.Ptt;
import tw.bjn.pg.ptt.PttItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class Addicting implements Skill {

    @Autowired
    private Ptt ptt;

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        return event.getMessage().getText().equals("@ptt");
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        List<PttItem> items = ptt.getLatest();
        List<Bubble> bubbles = new ArrayList<>();
        FlexMessage.FlexMessageBuilder builder = FlexMessage.builder();
        int count = 0;
        for (PttItem item : items) {
            URIAction a = new URIAction("read", item.getUrl(), null);
            bubbles.add(
                    Bubble.builder()
                            .body(Box.builder()
                                    .layout(FlexLayout.VERTICAL)
                                    .contents(Collections.singletonList(
                                            Text.builder().wrap(true).text(item.getTitle()).build())).build())
                            .footer(
                                    Box.builder()
                                            .layout(FlexLayout.VERTICAL)
                                            .contents(Collections.singletonList(Button.builder().height(Button.ButtonHeight.SMALL).style(Button.ButtonStyle.PRIMARY).action(a).build())).build())
                            .build());
            if (++ count >= 10) {
                log.warn("items more than 10, dropping");
                break;
            }
        }
        return builder.altText("ptt latest").contents(Carousel.builder().contents(bubbles).build()).build();
    }

    @Override
    public int priority() {
        return 0;
    }
}
