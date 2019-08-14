package tw.bjn.pg.handlers.postback;

import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.handlers.EventHandler;
import tw.bjn.pg.ptt.Ptt;
import tw.bjn.pg.ptt.model.PttResult;
import tw.bjn.pg.ptt.PttUtils;

import java.util.Map;

@Slf4j
@LineEventHandler
public class PostBackHandler extends EventHandler<PostbackEvent> {

    @Autowired
    private Ptt ptt;

    @Autowired
    private PttUtils pttUtils;

    @Override
    protected Message onEvent(PostbackEvent event) {
        log.info("get event {}", event);
        String data = event.getPostbackContent().getData();
        Map<String,String> params = event.getPostbackContent().getParams();

        if (data.startsWith(PttUtils.COMMAND_KEYWORD)) {
            String url = data.split(" ")[1];
            PttResult result = ptt.fetchPttFromURL(url);
            return pttUtils.parseResult(result);
        }
        return null;
    }
}
