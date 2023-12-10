package dev.Innocent.EventManagementSystem.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition
@SecurityScheme(
                name = "Bearer Authentication",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                scheme = "bearer"
        )
        public class SwaggerConfiguration {

    @Bean
    public OpenAPI config() {
        return new OpenAPI().info(
                new Info()
                        .title("Event Management System" +
                                "")
                        .version("V1.0")
                        .description("Security for Rest APIs... and MVCs")
        );
    }
}
