package com.noslen.paymentservice.controller.product;

import com.noslen.paymentservice.controller.BaseHandler;
import com.noslen.paymentservice.dto.ProductDto;
import com.stripe.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductHandlerImpl extends BaseHandler implements ProductHandler {

    public Mono<ServerResponse> listAll(ServerRequest r) { return null; }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest r) {
        return null;
    }
}
