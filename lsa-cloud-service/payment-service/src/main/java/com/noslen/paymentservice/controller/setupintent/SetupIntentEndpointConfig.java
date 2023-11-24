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

        return route((GET("/api/setupintent")),handler::listAll)
                .andRoute(GET("/api/setupintent/{id}"),handler::retrieve)
                .andRoute(POST("/api/setupintent"),handler::create)
                .andRoute(PUT("/api/setupintent/confirm/{id}"),handler::confirm)
                .andRoute(PUT("/api/setupintent/{id}"),handler::update)
                .andRoute(DELETE("/api/setupintent/{id}"),handler::cancel);
    }
}
