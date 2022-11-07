package com.noslen.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

import java.security.Principal;


@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository clientRegistrationRepository) throws Exception {

        OidcClientInitiatedServerLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");

        CookieServerCsrfTokenRepository csrfTokenRepository = new CookieServerCsrfTokenRepository();
        csrfTokenRepository.setCookiePath("/");

        // process CORS annotations
        http.cors();

        // force a non-empty response body for 401's to make the response more browser friendly

        return http
                .oauth2Login().and()

                .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository))

                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()

                .authorizeExchange()
                .anyExchange().authenticated()
                .and()

                .build();
    }

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> exchange.getPrincipal()
                .map(Principal::getName)
                .map(userName -> {
                    //adds header to proxied request
                    exchange.getRequest().mutate().header("X-Forward-User", userName).build();
                    return exchange;
                })
                .flatMap(chain::filter);
    }

}
