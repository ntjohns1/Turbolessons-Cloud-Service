package com.noslen.paymentservice.controller.product;

import com.noslen.paymentservice.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface ProductHandler {

    Mono<ServerResponse> listAll(ServerRequest r);

    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> create(ServerRequest r);

    Mono<ServerResponse> update(ServerRequest r);

    Mono<ServerResponse> delete(ServerRequest r);
}
