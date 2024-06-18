package com.noslen.paymentservice.controller.paymentmethod;

import com.noslen.paymentservice.dto.BankDto;
import com.noslen.paymentservice.dto.CardDto;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PaymentMethodHandler {
    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> retrieveByCustomer(ServerRequest r);

    Mono<ServerResponse> createCard(ServerRequest r);

    Mono<ServerResponse> createBank(ServerRequest r);

    Mono<ServerResponse> updateCard(ServerRequest r);

    Mono<ServerResponse> attach(ServerRequest r);

    Mono<ServerResponse> detach(ServerRequest r);
}
