package com.noslen.eventservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
                .anyRequest().authenticated() // all request require auth
                .and()
                .oauth2ResourceServer().jwt().and().and() // validate JWT bearer token from header
                .sessionManagement().disable() // no Sessions
                .build();
    }

}
