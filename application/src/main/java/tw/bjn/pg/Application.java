package tw.bjn.pg;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.spring.boot.LineBotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main( String args[] ){
        SpringApplication.run(Application.class, args);
    }
}

