package moodbuddy.moodbuddy.global.common.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.time.Duration;

@Configuration
public class GPTConfig {

    @Value("${gpt.api.key}")
    private String apiKey;

    @Bean(name = "gptWebClient")
    public WebClient gptWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
                .responseTimeout(Duration.ofSeconds(30))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30)));

        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(authorizationFilter())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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
