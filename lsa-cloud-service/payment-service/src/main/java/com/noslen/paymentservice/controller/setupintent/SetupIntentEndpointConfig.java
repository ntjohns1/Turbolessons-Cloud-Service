package com.noslen.paymentservice.controller.setupintent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SetupIntentEndpointConfig {

    private final SetupIntentHandler handler;

    public SetupIntentEndpointConfig(SetupIntentHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> routes() {

        return route((GET("")),handler::listAll)
                .andRoute(GET(""),handler::retrieve)
                .andRoute(POST(""),handler::create)
                .andRoute(PUT(""),handler::update)
                .andRoute(DELETE(""),handler::cancel);
    }
}
