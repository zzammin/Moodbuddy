package moodbuddy.moodbuddy.global.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
// http://localhost:8080/swagger-ui/index.html#/
public class SpringDocOpenApiConfig {
    @Bean
    public OpenAPI getOpenApi() {
        return new
                OpenAPI().components(new Components())
                .info(getInfo());

    }

    private Info getInfo() {
        return new Info()
                .version("1.0.0")
                .description("MoodBuddy API 명세서")
                .title("MoodBuddy");
    }
}
