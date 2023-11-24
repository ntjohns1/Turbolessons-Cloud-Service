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

        return route((GET("/api/price/standard")), handler::getStandardRate)
                .andRoute(GET("/api/price"),handler::listAll)
                .andRoute(GET("/api/price/{id}"),handler::retrieve)
                .andRoute(POST("/api/price"),handler::create)
                .andRoute(PUT("/api/price/{id}"),handler::update);
    }
}
