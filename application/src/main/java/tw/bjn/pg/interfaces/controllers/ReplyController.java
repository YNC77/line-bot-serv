package tw.bjn.pg.interfaces.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ReplyController extends LineBotController { // refactor
    // reply token:  message, follow, join, postback, beancon
    // TODO: need to limit function return value. SDK will throw exception if the event is not ReplyEvent
    public ReplyController(LineMessagingClient lineMessagingClient) {
        super(lineMessagingClient);
    }

    protected void reply(String replyToken, Message message) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, message))
                .whenComplete(this::onReplyDone);
    }

    protected void onReplyDone(BotApiResponse botApiResponse, Throwable throwable) {
        if( throwable != null )
            log.error("reply failed.", throwable);
        else
            log.info("response: {}", botApiResponse);
    }
}
