package moodbuddy.moodbuddy.global.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
@OpenAPIDefinition(
        info=@io.swagger.v3.oas.annotations.info.Info(title = "MoodBuddy",
                description = "MoodBuddy API 명세서",
                version ="1.0.0"))
@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }
}

//@OpenAPIDefinition
//@Configuration
// http://localhost:8080/swagger-ui/index.html#/
//public class SpringDocOpenApiConfig {
//    @Bean
//    public OpenAPI getOpenApi() {
//        return new
//                OpenAPI().components(new Components())
//                .info(getInfo());
//
//    }
//
//    private Info getInfo() {
//        return new Info()
//                .version("1.0.0")
//                .description("MoodBuddy API 명세서")
//                .title("MoodBuddy");
//    }
//}

