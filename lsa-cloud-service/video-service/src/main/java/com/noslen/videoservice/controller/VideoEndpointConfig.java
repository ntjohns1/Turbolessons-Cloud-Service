package com.noslen.videoservice.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class VideoEndpointConfig {

    @Bean
    RouterFunction<ServerResponse> routes(VideoHandler handler) {
        return route((GET("/api/messages")), handler::getVideo);
    }
}
