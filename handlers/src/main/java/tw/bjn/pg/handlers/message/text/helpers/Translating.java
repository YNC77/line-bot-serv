package tw.bjn.pg.handlers.message.text.helpers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.bjn.pg.translator.Translator;
import tw.bjn.pg.utils.MsgUtils;

@Component
public class Translating implements Skill{

    @Autowired
    Translator translator;

    @Autowired
    MsgUtils msgUtils;

    @Override
    public boolean isCapable(MessageEvent<TextMessageContent> event) {
        return event.getMessage().getText().matches("(?i)^:(aa|ab|ae|af|ak|am|an|ar|as|av|ay|az|ba|be|bg|bh|bi|bm|bn|bo|br|bs|ca|ce|ch|co|cr|cs|cu|cv|cy|da|de|dv|dz|ee|el|en|eo|es|et|eu|fa|ff|fi|fj|fo|fr|fy|ga|gd|gl|gn|gu|gv|ha|he|hi|ho|hr|ht|hu|hy|hz|ia|id|ie|ig|ii|ik|io|is|it|iu|ja|jv|ka|kg|ki|kj|kk|kl|km|kn|ko|kr|ks|ku|kv|kw|ky|la|lb|lg|li|ln|lo|lt|lu|lv|mg|mh|mi|mk|ml|mn|mr|ms|mt|my|na|nb|nd|ne|ng|nl|nn|no|nr|nv|ny|oc|oj|om|or|os|pa|pi|pl|ps|pt|qu|rm|rn|ro|ru|rw|sa|sc|sd|se|sg|si|sk|sl|sm|sn|so|sq|sr|ss|st|su|sv|sw|ta|te|tg|th|ti|tk|tl|tn|to|tr|ts|tt|tw|ty|ug|uk|ur|uz|ve|vi|vo|wa|wo|xh|yi|yo|za|zh|zu)\\s.+$");
    }

    @Override
    public Message perform(MessageEvent<TextMessageContent> event) {
        String text = event.getMessage().getText();
        String target = text.substring(1,3);
        String source = text.substring(4);
        String translated = translator.translate(source, target);
        return msgUtils.createTextMsg(translated);
    }

    @Override
    public int priority() {
        return 5;
    }
}
