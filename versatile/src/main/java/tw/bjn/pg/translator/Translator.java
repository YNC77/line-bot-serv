package tw.bjn.pg.translator;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Translator {

    private Translate translate;

    @PostConstruct
    public void init() {
        translate = TranslateOptions.getDefaultInstance().getService();
    }

    public String translate(String origin, String target) {
        Translation translation = translate.translate(origin,
                Translate.TranslateOption.targetLanguage(target));
        return translation.getTranslatedText();
    }
}
