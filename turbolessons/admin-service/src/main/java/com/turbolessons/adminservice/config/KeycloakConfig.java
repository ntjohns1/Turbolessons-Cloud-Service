package com.turbolessons.adminservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Wires the {@link RestTemplate} used by the Keycloak Admin REST client and
 * enables {@link KeycloakAdminProperties} binding.
 *
 * <p>{@code RestTemplate} (servlet/{@code javax}) is used deliberately instead of
 * {@code keycloak-admin-client} (which is {@code jakarta}-based on recent versions)
 * to stay compatible with this module's Spring Boot 2.7 / {@code javax} classpath.
 */
@Configuration
@EnableConfigurationProperties(KeycloakAdminProperties.class)
public class KeycloakConfig {

    @Bean
    public RestTemplate keycloakRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(15))
                .build();
    }
}
