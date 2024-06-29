package moodbuddy.moodbuddy.global.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NaverCloudConfig {
    @Value("${naver.cloud.client-id}")
    private String clientId;
    @Value("${naver.cloud.client-secret}")
    private String clientSecret;

    @Bean(name = "naverWebClient")
    public WebClient naverWebClient(){
        return WebClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize")
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", clientId)
                .defaultHeader("X-NCP-APIGW-API-KEY", clientSecret)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
