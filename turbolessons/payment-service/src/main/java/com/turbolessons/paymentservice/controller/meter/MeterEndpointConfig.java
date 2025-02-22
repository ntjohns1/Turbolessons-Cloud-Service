package com.turbolessons.paymentservice.controller.meter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MeterEndpointConfig {

    private final MeterHandler handler;

    public MeterEndpointConfig(MeterHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> usageRecordRoutes() {
        return route((POST("/api/payments/usage_record")),
                     handler::create);
    }
}
