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
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import tw.bjn.pg.ptt.Ptt;
import tw.bjn.pg.ptt.PttBoardItem;
import tw.bjn.pg.ptt.PttPostItem;
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

    private final String REPLY_COUNT_1_9 = "#00cc00";
    private final String REPLY_COUNT_10_99 = "#ffff66";
    private final String REPLY_EXPLODE = "#ff0000";

    private final int MAX_PO_PER_BUBBLE = 6 + 5;
    private final int MAX_BOARD_PER_BUBBLE = 8 + 7; // rows + separators
    private final int MAX_BUBBLE_COUNT = 10;

    private final Separator separator = Separator.builder().color(SEPARATOR_COLOR).build();

    @Autowired
    private Ptt ptt;

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        return event.getMessage().getText().startsWith(COMMAND_KEYWORD);
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        String command = event.getMessage().getText().trim();


        if (command.equals(COMMAND_KEYWORD)) {
            // TODO: show hot boards, /bbs/index.html
            return parseResult(ptt.fetchHotBoards());
        }

        // TODO: redesign arguments
        String [] args = command.substring(COMMAND_KEYWORD.length() + 1).split(" ");
        String board = args[0];
        String index = "";

        if (args.length > 1) {
            index = args[1];
        }

        PttResult result = ptt.fetchPttBoard(board, index);
        return parseResult(result);
    }

    private Message parseResult(PttResult result) {
        if (!CollectionUtils.isEmpty(result.getPttItemList()))
            return createMessageFromBoardContent(result);
        else if (!CollectionUtils.isEmpty(result.getPttBoardItems()))
            return createMessageFromBoardList(result);
        log.warn("No result to parse, going to return 'null' message");
        return null;
    }

    private Message createMessageFromBoardList(PttResult result) {
        List<PttBoardItem> items = result.getPttBoardItems();
        List<Bubble> bubbles = new ArrayList<>();
        FlexMessage.FlexMessageBuilder builder = FlexMessage.builder();

        List<FlexComponent> bodies = null;

        for (PttBoardItem item : items) {
            if (bodies == null)
                bodies = new ArrayList<>();
            else
                bodies.add(separator);

            bodies.add(genRowOfBoard(item));

            if (bodies.size() >= MAX_BOARD_PER_BUBBLE) {
                bubbles.add(rowsToBubble(bodies));
                bodies = null;
            }

            if (bubbles.size() >= MAX_BUBBLE_COUNT) {
                log.warn("maximum bubble count: 10, dropping remained");
                break;
            }
        }

        return builder.altText(COMMAND_KEYWORD).contents(Carousel.builder().contents(bubbles).build()).build();
    }

    private Box genRowOfBoard(PttBoardItem item) {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.MD)
                .action(new MessageAction("toBoard", COMMAND_KEYWORD + " " + item.getName()))
                .contents(Lists.newArrayList(
                        Box.builder()
                                .layout(FlexLayout.HORIZONTAL)
                                .spacing(FlexMarginSize.MD)
                                .contents(Lists.newArrayList(
                                        Text.builder()
                                                .text(item.getName())
                                                .color(TITLE_COLOR)
                                                .flex(0)
                                                .build(),
                                        separator,
                                        Text.builder()
                                                .text(item.getPopularity())
                                                .color(TITLE_COLOR)
                                                .build(),
                                        separator,
                                        Text.builder()
                                                .text(item.getClazz())
                                                .flex(0)
                                                .align(FlexAlign.END)
                                                .color(TITLE_COLOR)
                                                .build()))
                                .build(),
                        Text.builder()
                                .color(SUBTITLE_COLOR)
                                .text(item.getTitle())
                                .build()))
                .build();
    }

    private Message createMessageFromBoardContent(PttResult result) {
        List<PttPostItem> items = result.getPttItemList();
        List<Bubble> bubbles = new ArrayList<>();
        FlexMessage.FlexMessageBuilder builder = FlexMessage.builder();

        List<FlexComponent> bodies = new ArrayList<>();

        int maxItemCount = MAX_BUBBLE_COUNT * MAX_PO_PER_BUBBLE - 1; // reserved one for manipulation

        int counter = 0;
        for (PttPostItem item : items) {
            bodies.add(genRowOfPost(item));
            ++ counter;
            if (++ counter < MAX_PO_PER_BUBBLE)
                bodies.add(separator);
            else
                counter = 0;
            if (--maxItemCount <= 0) break;
        }
        bodies.add(genNavigationRow(result));

        int to = 0;
        while (to < bodies.size()) {
            int from = to;
            while (++to < bodies.size() && to - from < MAX_PO_PER_BUBBLE);
            bubbles.add(rowsToBubble(bodies.subList(from, to)));
        }

        return builder.altText(COMMAND_KEYWORD).contents(Carousel.builder().contents(bubbles).build()).build();
    }

    private Box genNavigationRow(PttResult result) {
        List<FlexComponent> lastBubbleItems = new ArrayList<>();
        if (!result.getPrevPageUrl().isEmpty()) {
            lastBubbleItems.add(
                    Button.builder()
                            .style(Button.ButtonStyle.LINK)
                            .gravity(FlexGravity.BOTTOM)
                            .height(Button.ButtonHeight.SMALL)
                            .flex(1)
                            .action(new MessageAction("More", COMMAND_KEYWORD + " " + result.getBoard() + " " + result.getPrevIndex()))
                            .build()
            );
        }
        lastBubbleItems.add(separator);
        lastBubbleItems.add(Button.builder()
                .style(Button.ButtonStyle.LINK)
                .gravity(FlexGravity.BOTTOM)
                .flex(1)
                .height(Button.ButtonHeight.SMALL)
                .action(new MessageAction("Home", COMMAND_KEYWORD))
                .build());
        return Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .spacing(FlexMarginSize.MD)
                .contents(lastBubbleItems)
                .build();
    }

    private Box genRowOfPost(PttPostItem item) {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.MD)
//                .flex(0)
                .contents(Lists.newArrayList(
                        Box.builder()
                                .flex(0)
                                .layout(FlexLayout.HORIZONTAL)
                                .spacing(FlexMarginSize.MD)
                                .action(new URIAction("read", item.getUrl(), null))
                                .contents(Lists.newArrayList(
                                        Text.builder()
                                                .text(item.getDate())
                                                .size(FlexFontSize.SM)
                                                .align(FlexAlign.CENTER)
                                                .flex(0)
                                                .color(SUBTITLE_COLOR)
                                                .build(),
                                        separator,
                                        Text.builder()
                                                .text(item.getReply())
                                                .size(FlexFontSize.SM)
                                                .align(FlexAlign.CENTER)
                                                .color(replyColor(item.getReply()))
                                                .weight(Text.TextWeight.BOLD)
                                                .flex(0)
                                                .build(),
                                        separator,
                                        Text.builder()
                                                .text(item.getAuthor())
                                                .size(FlexFontSize.SM)
                                                .align(FlexAlign.START)
                                                .color(SUBTITLE_COLOR)
                                                .build()))
                                .build(),
                        Text.builder()
                                .flex(1)
                                .wrap(true)
                                .text(item.getTitle())
                                .color(TITLE_COLOR)
                                .build()))
                .build();
    }

    private String replyColor(String reply) {
        if (reply.contains("X"))
            return SUBTITLE_COLOR;
        if (reply.matches("[1-9]"))
            return REPLY_COUNT_1_9;
        if (reply.matches("[1-9][0-9]"))
            return REPLY_COUNT_10_99;
        return REPLY_EXPLODE;
    }

    private Bubble rowsToBubble(List<FlexComponent> bodies) {
        return Bubble.builder()
                .styles(BubbleStyles.builder()
                        .body(BubbleStyles.BlockStyle.builder()
                                .backgroundColor(BACKGROUND_COLOR)
                                .build())
                        .build())
                .body(Box.builder()
                        .spacing(FlexMarginSize.MD)
                        .layout(FlexLayout.VERTICAL)
                        .contents(bodies)
                        .build())
                .build();
    }

    @Override
    public int priority() {
        return 0;
    }
}
