package com.turbolessons.paymentservice.controller.usagerecord;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface UsageRecordHandler {
    Mono<ServerResponse> create(ServerRequest r);
}
