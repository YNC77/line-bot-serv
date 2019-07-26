package tw.bjn.pg.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.interfaces.controllers.ReplyController;

@Slf4j
@LineMessageHandler
public class PostbackController extends ReplyController {

    @Autowired
    public PostbackController(LineMessagingClient lineMessagingClient) {
        super(lineMessagingClient);
    }
}
