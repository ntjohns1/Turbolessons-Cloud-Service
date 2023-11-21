package com.noslen.paymentservice.controller.price;

import jakarta.ws.rs.GET;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PriceEndpointConfig {

    private final PriceHandler handler;

    public PriceEndpointConfig(PriceHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> routes() {

        return route((GET("")), handler::getStandardRate)
                .andRoute(GET(""),handler::listAll)
                .andRoute(GET(""),handler::retrieve)
                .andRoute(POST(""),handler::create)
                .andRoute(PUT(""),handler::update);
    }
}
