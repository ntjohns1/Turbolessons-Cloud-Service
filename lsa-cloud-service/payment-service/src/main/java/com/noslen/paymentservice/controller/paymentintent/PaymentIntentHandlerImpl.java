package com.noslen.paymentservice.controller.paymentintent;

import com.noslen.paymentservice.controller.BaseHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class PaymentIntentHandlerImpl extends BaseHandler implements PaymentIntentHandler {

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> searchByCustomer(ServerRequest r) {
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
    public Mono<ServerResponse> capture(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> cancel(ServerRequest r) {
        return null;
    }
}
