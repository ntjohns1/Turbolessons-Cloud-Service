package com.noslen.paymentservice.controller;

import com.noslen.paymentservice.service.SubscriptionService;
import com.stripe.model.StripeCollection;
import com.stripe.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SubscriptionHandler {

    private final SubscriptionService subscriptionService;


    public SubscriptionHandler(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    Mono<ServerResponse> listAll(ServerRequest r) {
        return listAllResponse(this.subscriptionService.listAllSubscriptions());
    }

    private Mono<ServerResponse> listAllResponse(Publisher<StripeCollection<Subscription>> customers) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customers,
                      StripeCollection.class)
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable e) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Error message or object"));
    }
}
