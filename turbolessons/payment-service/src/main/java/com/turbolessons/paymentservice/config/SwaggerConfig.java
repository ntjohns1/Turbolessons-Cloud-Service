package com.turbolessons.paymentservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:payment-service}")
    private String applicationName;

    @Bean
    public OpenAPI paymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TurboLessons Payment Service API")
                        .description("REST API for managing payments through Stripe integration")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("TurboLessons Support")
                                .url("https://turbolessons.com/support")
                                .email("support@turbolessons.com"))
                        .license(new License()
                                .name("Private")
                                .url("https://turbolessons.com/terms")))
                .externalDocs(new ExternalDocumentation()
                        .description("TurboLessons Documentation")
                        .url("https://turbolessons.com/docs"))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Current environment")
                ));
    }
}
