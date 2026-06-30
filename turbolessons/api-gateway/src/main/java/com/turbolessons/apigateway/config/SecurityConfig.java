package com.turbolessons.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

    /**
     * CORS origins allowed by the gateway, supplied per-environment by the config
     * server (cors.allowed-origins in api-gateway.yml / api-gateway-qac.yml).
     */
    @Value("${cors.allowed-origins:https://www.turbolessons.com,http://localhost:3000}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http, ReactiveJwtAuthenticationConverter jwtConverter) {
        // Pure resource server: the SPA logs in with Keycloak and sends a bearer
        // token. No interactive oauth2Login — unauthenticated calls get a clean
        // 401 instead of a redirect to /oauth2/authorization/keycloak. CSRF is
        // disabled because it's a stateless bearer API (WebFlux enables CSRF by
        // default, which 403s every POST/PUT/DELETE the SPA sends with no token).
        http
                // Wire the corsConfigurationSource bean into the chain and let
                // CORS preflight (OPTIONS) through, so cross-origin browser calls
                // aren't 403'd at the preflight.
                .cors().and()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .pathMatchers("/ws/**").permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtConverter);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setMaxAge(3600L);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

}
