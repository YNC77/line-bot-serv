package tw.bjn.pg.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.interfaces.controllers.ReplyController;

@Slf4j
@LineMessageHandler
public class MessageController extends ReplyController {

    @Autowired
    public MessageController(LineMessagingClient lineMessagingClient) {
        super(lineMessagingClient);
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: {}", event);
        reply(event.getReplyToken(), new TextMessage(event.getMessage().getText()));
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        log.info("event: {}", event);
        reply(event.getReplyToken(), new TextMessage("sticker: '" + event.getMessage().getPackageId() + "'-'" + event.getMessage().getStickerId()));
    }

    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) {
        log.info("event: {}", event);
        // ContentProvider may not provide resource URL for us
        reply(event.getReplyToken(), new TextMessage(event.getMessage().getId()));
    }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        log.info("event: {}", event);
        // ContentProvider may not provide resource URL for us
        reply(event.getReplyToken(), new TextMessage(event.getMessage().getId()));
    }

    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) {
        log.info("event: {}", event);
        // ContentProvider may not provide resource URL for us
        reply(event.getReplyToken(), new TextMessage(event.getMessage().getId()));
    }

    @EventMapping
    public void handleFileMessageEvent(MessageEvent<FileMessageContent> event) {
        log.info("event: {}", event);
        reply(event.getReplyToken(), new TextMessage(event.getMessage().getFileName()));
    }
}
