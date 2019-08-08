package tw.bjn.pg.handlers.message.text.helpers;

import com.google.common.collect.Lists;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.ptt.Ptt;
import tw.bjn.pg.ptt.PttItem;
import tw.bjn.pg.ptt.PttResult;

import java.util.ArrayList;
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
        PttResult result = ptt.getLatest();
        List<PttItem> items = result.getPttItemList();
        List<Bubble> bubbles = new ArrayList<>();
        FlexMessage.FlexMessageBuilder builder = FlexMessage.builder();

        List<FlexComponent> bodies = null;

        for (PttItem item : items) {

            URIAction a = new URIAction("read", item.getUrl(), null);

            if (bodies == null)
                bodies = new ArrayList<>();
            else
                bodies.add(
                        Separator.builder()
                                .margin(FlexMarginSize.XL)
                                .build()
                );

            Box box = Box.builder()
                    .layout(FlexLayout.VERTICAL)
                    .contents(
                            Lists.newArrayList(
                                    Box.builder()
                                            .layout(FlexLayout.HORIZONTAL)
                                            .contents(
                                                    Lists.newArrayList(
                                                            Text.builder()
                                                            .text(item.getDate().trim()).build(),
                                                            Separator.builder().build(),
                                                            Text.builder()
                                                            .text(item.getReply()).build(),
                                                            Separator.builder().build(),
                                                            Text.builder()
                                                            .text(item.getAuthor()).build()
                                                    )
                                            )
                                            .build(),
                                    Text.builder()
                                            .wrap(true)
                                            .text(item.getTitle())
                                            .weight(Text.TextWeight.BOLD)
                                            .action(a)
                                            .flex(1)
                                            .build()
                            )
                    )
                    .build();

            bodies.add(box);

            if (bodies.size() >= 7) {
                bubbles.add(
                    Bubble.builder()
                            .body(Box.builder()
                                    .spacing(FlexMarginSize.LG)
                                    .layout(FlexLayout.VERTICAL)
                                    .contents(bodies)
                                    .build()
                            ).build()
                );
                bodies = null;
            }

            if (bubbles.size() >= 9) {
                log.warn("carourel message can only have 10 bubbles, reserve 1 for last page, dropping remained");
                break;
            }
        }
        // build last page
        List<FlexComponent> lastBubbleItems = new ArrayList<>();
        if (!result.getPrevPageUrl().isEmpty()) {
            lastBubbleItems.add(
                    Button.builder()
                            .style(Button.ButtonStyle.PRIMARY)
                            .action(new URIAction("Prev", result.getPrevPageUrl(), null))
                            .build()
            );
        }
        if (!result.getNextPageUrl().isEmpty()) {
            lastBubbleItems.add(
                    Button.builder()
                            .style(Button.ButtonStyle.SECONDARY)
                            .action(new URIAction("Next", result.getNextPageUrl(), null))
                            .build()
            );
        }
        if (!result.getLatestUrl().isEmpty()) {
            lastBubbleItems.add(
                    Button.builder()
                            .style(Button.ButtonStyle.LINK)
                            .action(new URIAction("Latest", result.getLatestUrl(), null))
                            .build()
            );
        }
        if (!lastBubbleItems.isEmpty())
            bubbles.add(
                    Bubble.builder()
                            .body(Box.builder()
                                    .layout(FlexLayout.VERTICAL)
                                    .contents(lastBubbleItems)
                                    .build())
                            .build());
        return builder.altText("ptt-latest").contents(Carousel.builder().contents(bubbles).build()).build();
    }

    @Override
    public int priority() {
        return 0;
    }
}
