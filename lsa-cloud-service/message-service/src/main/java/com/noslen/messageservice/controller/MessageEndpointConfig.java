package com.noslen.messageservice.controller;

import com.noslen.messageservice.config.CaseInsensitiveRequestPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class MessageEndpointConfig {

    @Bean
    RouterFunction<ServerResponse> routes(MessageHandler handler) {
        return route(i(GET("/api/messages")), handler::all)
                .andRoute(i(GET("/api/messages/{id}")), handler::getById)
                .andRoute(i(DELETE("/api/messages/{id}")), handler::deleteById)
                .andRoute(i(POST("/api/messages")), handler::create);
    }


    private static RequestPredicate i(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}
