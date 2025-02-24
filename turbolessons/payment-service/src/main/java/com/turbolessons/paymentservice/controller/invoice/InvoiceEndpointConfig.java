package com.turbolessons.paymentservice.controller.invoice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class InvoiceEndpointConfig {

    private final InvoiceHandler handler;

    public InvoiceEndpointConfig(InvoiceHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> meterRoutes() {
        return route((GET("/api/payments/invoice")), handler::listAll)
                .andRoute(GET("/api/payments/invoice/customer_list/{id}"), handler::listAllByCustomer)
                .andRoute(GET("/api/payments/invoice/subscription_list/{id}"), handler::listAllBySubscription)
                .andRoute(GET("/api/payments/invoice/{id}"), handler::retrieve)
                .andRoute(GET("/api/payments/invoice/upcoming/{id}"), handler::retrieveUpcoming)
                .andRoute(POST("/api/payments/invoice"), handler::create)
                .andRoute(PUT("/api/payments/invoice/{id}"), handler::update)
                .andRoute(DELETE("/api/payments/invoice/{id}"), handler::deleteDraft)
                .andRoute(GET("/api/payments/invoice/subscription_list/{id}"), handler::finalize)
                ;
    }
}
