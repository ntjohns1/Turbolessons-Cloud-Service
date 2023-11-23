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
       return route((GET("/api/paymentmethod/{id}")), handler::retrieve)
                .andRoute(GET("/api/paymentmethod/customer/{id}"), handler::retrieveByCustomer)
                .andRoute(POST("/api/paymentmethod/card/{id}"), handler::createCard)
                .andRoute(POST("/api/paymentmethod/bank/{id}"), handler::createBank)
                .andRoute(PUT("/api/paymentmethod/{id}"), handler::updateCard)
                .andRoute(PUT("/api/paymentmethod/attach/{id}/{customerId}"),handler::attach)
                .andRoute(PUT("/api/paymentmethod/detach/{id}"),handler::detach);
    }
}
