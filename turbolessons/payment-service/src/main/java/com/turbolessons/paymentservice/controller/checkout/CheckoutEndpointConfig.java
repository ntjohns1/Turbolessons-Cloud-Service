package com.turbolessons.paymentservice.controller.checkout;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CheckoutEndpointConfig {

    private final CheckoutHandler handler;

    public CheckoutEndpointConfig(CheckoutHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> checkoutRoutes() {
        return route(POST("/api/payments/checkout/session"), handler::createCheckoutSession)
                .andRoute(POST("/api/payments/portal/session"), handler::createPortalSession);
    }
}
