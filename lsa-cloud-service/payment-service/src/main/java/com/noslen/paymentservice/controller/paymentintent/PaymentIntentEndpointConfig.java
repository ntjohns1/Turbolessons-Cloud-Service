package com.noslen.paymentservice.controller.paymentintent;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PaymentIntentEndpointConfig {

    private final PaymentIntentHandler handler;

    public PaymentIntentEndpointConfig(PaymentIntentHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> routes() {

      return  route((GET("/api/paymentintent")), handler::listAll)
                .andRoute(GET("/api/paymentintent/{id}"), handler::retrieve)
                .andRoute(POST("/api/paymentintent"), handler::create)
                .andRoute(PUT("/api/paymentintent/{id}"), handler::update)
                .andRoute(PUT("/api/paymentintent/{id}"), handler::capture)
                .andRoute(DELETE("/api/paymentintent/{id}"), handler::cancel);
    }
}
