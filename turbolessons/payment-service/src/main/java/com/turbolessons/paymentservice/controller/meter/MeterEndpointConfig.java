package com.turbolessons.paymentservice.controller.meter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MeterEndpointConfig {

    private final MeterHandler handler;

    public MeterEndpointConfig(MeterHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> meterRoutes() {
        return route((GET("/api/payments/meter")), handler::listAll)
                .andRoute(GET("/api/payments/meter/{id}"), handler::retrieve)
                .andRoute(POST("/api/payments/meter"), handler::create)
                .andRoute(PUT("/api/payments/meter/{id}"), handler::update)
                .andRoute(DELETE("/api/payments/meter/{id}"), handler::deactivate)
                .andRoute(DELETE("/api/payments/meter/{id}"), handler::reactivate)
                .andRoute(POST("/api/payments/meter_event"), handler::create);
    }
}
