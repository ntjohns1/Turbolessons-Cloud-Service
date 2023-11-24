package com.noslen.paymentservice.controller.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductEndpointConfig {

    private final ProductHandler handler;

    public ProductEndpointConfig(ProductHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> routes() {

        return route((GET("/api/product")),handler::listAll)
                .andRoute(GET("/api/product/{id}"),handler::retrieve)
                .andRoute(POST("/api/product"),handler::create)
                .andRoute(PUT("/api/product/{id}"),handler::update)
                .andRoute(DELETE("/api/product/{id}"),handler::delete);
    }
}
