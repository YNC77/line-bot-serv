package tw.bjn.pg.controllers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.flows.EnqueueFlow;
import tw.bjn.pg.interfaces.controllers.EnqueueController;

@Slf4j
@LineMessageHandler
public class MessageController extends EnqueueController {

    @Autowired
    MessageController(EnqueueFlow flow) {
        super(flow);
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        processEvent(event);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        processEvent(event);
    }

    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) {
        processEvent(event);
    }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        processEvent(event);
    }

    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) {
        processEvent(event);
    }

    @EventMapping
    public void handleFileMessageEvent(MessageEvent<FileMessageContent> event) {
        processEvent(event);
    }
}
