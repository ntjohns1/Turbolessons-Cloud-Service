package com.noslen.paymentservice.controller.paymentmethod;


import com.noslen.paymentservice.controller.BaseHandler;
import com.noslen.paymentservice.dto.BankDto;
import com.noslen.paymentservice.dto.CardDto;
import com.noslen.paymentservice.service.paymentintent.PaymentIntentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PaymentMethodHandlerImpl extends BaseHandler implements PaymentMethodHandler {

    private final PaymentIntentService paymentIntentService;

    public PaymentMethodHandlerImpl(PaymentIntentService paymentIntentService) {
        this.paymentIntentService = paymentIntentService;
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> retrieveByCustomer(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> createCard(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> createBank(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> updateCard(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> attach(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> detach(ServerRequest r) {
        return null;
    }
}
