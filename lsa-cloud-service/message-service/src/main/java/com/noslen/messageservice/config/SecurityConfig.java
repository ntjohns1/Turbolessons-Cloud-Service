package com.noslen.messageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

@EnableWebFluxSecurity // <1>
@EnableReactiveMethodSecurity // <2>
public class SecurityConfig {

    @Bean // <3>
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        return http
                .csrf()
                    .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()) // <4>
                    .and()
                .authorizeExchange()
                    .pathMatchers("/ws/**").permitAll() // <5>
                    .anyExchange().authenticated()
                    .and()
                .oauth2Login()
                    .and()
                .oauth2ResourceServer()
                    .jwt().and().and().build();
        // @formatter:on
    }

//    @Bean // <6>
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//        configuration.setAllowedMethods(Collections.singletonList("GET"));
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
