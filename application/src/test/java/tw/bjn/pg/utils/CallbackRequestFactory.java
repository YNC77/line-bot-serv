package tw.bjn.pg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Base64;
import java.util.function.Function;

@Profile("test")
@Component
public class CallbackRequestFactory {

    @Value("${line.bot.channel-secret}")
    private String secret;

    private Function<byte[], byte[]> getSignFunction;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        LineSignatureValidator validator = new LineSignatureValidator(secret.getBytes());
        getSignFunction = validator::generateSignature;
        objectMapper = ModelObjectMapper.createNewObjectMapper();
    }

    public <T extends MessageContent> String createMessageRequest(T message) {
      try {
          Event event = new MessageEvent<>("Replytoken",
                  new UserSource("Uuserid"),
                  message,
                  Instant.now());
          CallbackRequest request = new CallbackRequest(Lists.newArrayList(event), "dest");
          return objectMapper.writeValueAsString(request);
      } catch (JsonProcessingException e) {
          System.out.println("Create json failed");
          e.printStackTrace();
          return null;
      }
    }

    public String getBotSignature(byte[] json) {
        byte[] signature = Base64.getEncoder().encode(getSignFunction.apply(json));
        return new String(signature);
    }

}
