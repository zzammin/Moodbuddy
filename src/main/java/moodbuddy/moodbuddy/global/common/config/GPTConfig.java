package moodbuddy.moodbuddy.global.common.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class GPTConfig {
    @Value("${gpt.api.key}")
    private String apiKey;

    @Bean(name = "gptWebClient")
    public WebClient gptWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .filter(authorizationFilter())
                .build();
    }

    private ExchangeFilterFunction authorizationFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            return Mono.just(ClientRequest.from(clientRequest)
                    .header("Authorization", "Bearer " + apiKey)
                    .build());
        });
    }
}
