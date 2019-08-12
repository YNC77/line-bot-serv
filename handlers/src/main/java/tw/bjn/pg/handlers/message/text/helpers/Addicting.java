package tw.bjn.pg.handlers.message.text.helpers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.ptt.*;

@Slf4j
@Component
public class Addicting implements Skill {

    @Autowired
    private Ptt ptt;

    @Autowired
    private PttUtils pttUtils;

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        return event.getMessage().getText().startsWith(PttUtils.COMMAND_KEYWORD);
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        String commandLine = event.getMessage().getText().trim();

        // @ptt [board_name] [query_str]
        String [] args = commandLine.split(" ");
        if (args.length == 1) // no args
            return pttUtils.parseResult(ptt.fetchHotBoards());
//        String cmd = args[0];
        String board = args[1];
        if (args.length > 2) {
            String query = args[2];
            PttResult result = ptt.searchPttBoard(board, query);
            return pttUtils.parseResult(result);
        }
        PttResult result = ptt.fetchPttBoard(board);
        return pttUtils.parseResult(result);
    }

    @Override
    public int priority() {
        return 0;
    }
}
