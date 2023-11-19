package com.noslen.paymentservice.controller;


import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface RequestHandler<T> {
    Mono<ServerResponse> handle(ServerRequest request, T service);

}

