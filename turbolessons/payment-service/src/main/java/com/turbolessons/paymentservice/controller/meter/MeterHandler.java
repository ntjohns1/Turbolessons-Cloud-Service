package com.turbolessons.paymentservice.controller.meter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface MeterHandler {
    Mono<ServerResponse> create(ServerRequest r);
}
