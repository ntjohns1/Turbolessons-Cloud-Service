package com.noslen.paymentservice.controller.customer;


        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.reactive.function.server.RouterFunction;
        import org.springframework.web.reactive.function.server.ServerResponse;

        import static org.springframework.web.reactive.function.server.RequestPredicates.*;
        import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CustomerEndpointConfig {

    private final CustomerHandler handler;

    public CustomerEndpointConfig(CustomerHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> customerRoutes() {

        return route((GET("/api/customer")), handler::listAll)
                .andRoute(GET("/api/customer/{id}"), handler::retrieve)
                .andRoute(POST("/api/customer"), handler::create)
                .andRoute(PUT("/api/customer/{id}"), handler::update)
                .andRoute(DELETE("/api/customer/{id}"), handler::delete);
    }
}
