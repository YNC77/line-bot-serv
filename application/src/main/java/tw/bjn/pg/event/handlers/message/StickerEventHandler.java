package tw.bjn.pg.event.handlers.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;
import tw.bjn.pg.interfaces.event.EventHandler;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class StickerEventHandler extends EventHandler<MessageEvent<StickerMessageContent>> {

    class StickerPackage{
        int packageId;
        int startId;
        int endId;
        StickerPackage(int packageId, int startId, int endId) {
            this.packageId = packageId;
            this.startId = startId;
            this.endId = endId;
        }

        public String getPackageId() {
            return String.valueOf(packageId);
        }

        public String randomSticker() {
            return String.valueOf(randomRange(startId, endId));
        }
    }

    List<StickerPackage> availables = new ArrayList<>();

    int randomRange(int min, int max) {
        return new Random().nextInt(max-min+1)+min;
    }

    @PostConstruct
    public void init() {
        availables.add(new StickerPackage(11537, 52002734, 52002779));
        availables.add(new StickerPackage(11538, 51626494, 51626533));
        availables.add(new StickerPackage(11539, 52114110, 52114149));
    }

    @Override
    protected Message onEvent(MessageEvent<StickerMessageContent> event) {
        StickerPackage pack = availables.get( randomRange(0, availables.size()-1) );
        return new StickerMessage(pack.getPackageId(), pack.randomSticker());
    }
}
