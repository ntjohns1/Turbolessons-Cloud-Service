package com.noslen.paymentservice.controller.price;

import com.noslen.paymentservice.dto.PriceDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PriceHandler {
//    Mono<ServerResponse> getStandardRate(ServerRequest r);

    Mono<ServerResponse> listAll(ServerRequest r);

    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> create(ServerRequest r);

    Mono<ServerResponse> update(ServerRequest r);
}
