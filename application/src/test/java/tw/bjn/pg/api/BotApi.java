package tw.bjn.pg.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("bot-api")
public interface BotApi {
    @RequestLine("POST /callback")
    @Headers({"Content-Type: application/json", "X-Line-Signature: {sign}"})
    String callback(String json, @Param("sign") String sign);
}
