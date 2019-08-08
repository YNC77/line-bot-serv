package tw.bjn.pg.handlers.message.text.helpers;

import com.google.common.collect.Lists;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.BubbleStyles;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.*;
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

    private final String COMMAND_KEYWORD = "@ptt";
    private final String BACKGROUND_COLOR = "#080808";
    private final String TITLE_COLOR = "#eeeeee";
    private final String SUBTITLE_COLOR = "#bbbbbb";
    private final String SEPARATOR_COLOR = "#a0a0a0";

    @Autowired
    private Ptt ptt;

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        return event.getMessage().getText().startsWith(COMMAND_KEYWORD);
    }

    private Separator separator() {
        return Separator.builder()
                .color(SEPARATOR_COLOR)
                .build();
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        String command = event.getMessage().getText().trim();

        String board = "Gossiping";
        String index = "";

        if (command.equals(COMMAND_KEYWORD)) {
            // TODO: show hot boards, /bbs/index.html
            // return;
        } else {
            // TODO: redesign arguments
            String [] args = command.substring(COMMAND_KEYWORD.length() + 1).split(" ");
            board = args[0];

            if (args.length > 1) {
                index = args[1];
            }
        }


        PttResult result = ptt.fetchFromPttWeb(board, index);
        return parseResult(result);
    }

    private Message parseResult(PttResult result) {
        List<PttItem> items = result.getPttItemList();
        List<Bubble> bubbles = new ArrayList<>();
        FlexMessage.FlexMessageBuilder builder = FlexMessage.builder();

        List<FlexComponent> bodies = null;

        for (PttItem item : items) {
            URIAction a = new URIAction("read", item.getUrl(), null);

            if (bodies == null)
                bodies = new ArrayList<>();
            else
                bodies.add(separator());

            Box box = Box.builder()
                    .layout(FlexLayout.VERTICAL)
                    .spacing(FlexMarginSize.MD)
                    .contents(
                            Lists.newArrayList(
                                    Box.builder()
                                            .flex(1)
                                            .layout(FlexLayout.HORIZONTAL)
                                            .spacing(FlexMarginSize.MD)
                                            .contents(
                                                    Lists.newArrayList(
                                                            Text.builder()
                                                                    .text(item.getDate())
                                                                    .size(FlexFontSize.SM)
                                                                    .align(FlexAlign.CENTER)
                                                                    .flex(0)
                                                                    .color(SUBTITLE_COLOR)
                                                                    .build(),
                                                            separator(),
                                                            Text.builder()
                                                                    .text(item.getReply())
                                                                    .size(FlexFontSize.SM)
                                                                    .align(FlexAlign.CENTER)
                                                                    .flex(0)
                                                                    .color(SUBTITLE_COLOR)
                                                                    .build(),
                                                            separator(),
                                                            Text.builder()
                                                                    .text(item.getAuthor())
                                                                    .size(FlexFontSize.SM)
                                                                    .align(FlexAlign.CENTER)
                                                                    .color(SUBTITLE_COLOR)
                                                                    .build()
                                                    )
                                            )
                                            .build(),
                                    Text.builder()
                                            .wrap(true)
                                            .text(item.getTitle())
                                            .weight(Text.TextWeight.BOLD)
                                            .color(TITLE_COLOR)
                                            .action(a)
                                            .build()
                            )
                    )
                    .build();

            bodies.add(box);

            if (bodies.size() >= 9) {
                bubbles.add(
                    Bubble.builder()
                            .styles(BubbleStyles.builder()
                                    .body(BubbleStyles.BlockStyle.builder()
                                            .backgroundColor(BACKGROUND_COLOR)
                                            .build())
                                    .build())
                            .body(Box.builder()
                                    .spacing(FlexMarginSize.MD)
                                    .layout(FlexLayout.VERTICAL)
                                    .contents(bodies)
                                    .build()
                            ).build()
                );
                bodies = null;
            }

            if (bubbles.size() >= 9) {
                log.warn("carousel message can only have 10 bubbles, reserve 1 for last page, dropping remained");
                break;
            }
        }
        // build last page
        List<FlexComponent> lastBubbleItems = new ArrayList<>();
        if (!result.getPrevPageUrl().isEmpty()) {
            lastBubbleItems.add(
                    Button.builder()
                            .style(Button.ButtonStyle.LINK)
                            .gravity(FlexGravity.CENTER)
                            .flex(1)
                            .action(new MessageAction("Prev", COMMAND_KEYWORD + " " + result.getBoard() + " " + result.getPrevIndex()))
                            .build()
            );
        }
        if (!result.getNextPageUrl().isEmpty()) {
            lastBubbleItems.add(
                    Button.builder()
                            .style(Button.ButtonStyle.LINK)
                            .gravity(FlexGravity.CENTER)
                            .flex(1)
                            .action(new MessageAction("Next", COMMAND_KEYWORD + " " + result.getBoard() + " " + result.getNextIndex()))
                            .build()
            );
        }
        if (!result.getLatestUrl().isEmpty()) {
            lastBubbleItems.add(
                    Button.builder()
                            .style(Button.ButtonStyle.LINK)
                            .gravity(FlexGravity.CENTER)
                            .flex(1)
                            .action(new MessageAction("End", COMMAND_KEYWORD))
                            .build()
            );
        }
        if (!lastBubbleItems.isEmpty())
            bubbles.add(
                    Bubble.builder()
                            .styles(BubbleStyles.builder()
                                    .body(BubbleStyles.BlockStyle.builder()
                                            .backgroundColor(BACKGROUND_COLOR)
                                            .build())
                                    .build())
                            .body(Box.builder()
                                    .layout(FlexLayout.VERTICAL)
                                    .contents(lastBubbleItems)
                                    .build())
                            .build());
        return builder.altText("@ptt").contents(Carousel.builder().contents(bubbles).build()).build();
    }

    @Override
    public int priority() {
        return 0;
    }
}
