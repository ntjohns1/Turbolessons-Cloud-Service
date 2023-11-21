package com.noslen.paymentservice.controller.paymentmethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PaymentMethodEndpointConfig {

    private final PaymentMethodHandler handler;

    public PaymentMethodEndpointConfig(PaymentMethodHandler handler) {
        this.handler = handler;
    }


    @Bean
    RouterFunction<ServerResponse> routes() {
       return route((GET("")), handler::retrieve)
                .andRoute(GET(""), handler::retrieveByCustomer)
                .andRoute(POST(""), handler::createCard)
                .andRoute(POST(""), handler::createBank)
                .andRoute(GET(""), handler::updateCard)
                .andRoute(PUT(""),handler::attach)
                .andRoute(PUT(""),handler::detach);
    }
}
