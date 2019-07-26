package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
public class MessageController extends ReplyController {

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: {}", event);
        return new TextMessage(event.getMessage().getText());
    }

    @EventMapping
    public Message handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        log.info("event: {}", event);
        return new TextMessage("sticker: '" + event.getMessage().getPackageId() + "'-'" + event.getMessage().getStickerId());
    }

    @EventMapping
    public Message handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) {
        log.info("event: {}", event);
        // ContentProvider may not provide resource URL for us
        return new TextMessage(event.getMessage().getId());
    }

    @EventMapping
    public Message handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        log.info("event: {}", event);
        // ContentProvider may not provide resource URL for us
        return new TextMessage(event.getMessage().getId());
    }

    @EventMapping
    public Message handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) {
        log.info("event: {}", event);
        // ContentProvider may not provide resource URL for us
        return new TextMessage(event.getMessage().getId());
    }

    @EventMapping
    public Message handleFileMessageEvent(MessageEvent<FileMessageContent> event) {
        log.info("event: {}", event);
        return new TextMessage(event.getMessage().getFileName());
    }
}
