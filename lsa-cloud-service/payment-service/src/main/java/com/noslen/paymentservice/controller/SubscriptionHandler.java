package com.noslen.paymentservice.controller;

import com.noslen.paymentservice.dto.CustomerDto;
import com.noslen.paymentservice.dto.SubscriptionDto;
import com.noslen.paymentservice.service.SubscriptionService;
import com.stripe.model.Subscription;
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
        return this.subscriptionService.listAllSubscriptions()
                .flatMap(subscriptions -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(subscriptions))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    Mono<ServerResponse> retrieve(ServerRequest r) {
        return this.subscriptionService.retrieveSubscription(id(r))
                .flatMap(subscription -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(subscription))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }


    private Mono<ServerResponse> listAllResponse(Publisher<StripeCollection<Subscription>> subscriptions) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(subscriptions,
                      StripeCollection.class)
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> defaultReadResponse(Publisher<Subscription> subscription) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(subscription,
                      Subscription.class)
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> defaultWriteResponse(Mono<Subscription> subscriptionMono) {
        return subscriptionMono.flatMap(subscription -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(subscription)))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> voidResponse(ServerRequest r) {
        return ServerResponse.noContent()
                .build()
                .onErrorResume(this::handleError);
    }    Mono<ServerResponse> create(ServerRequest r) {
        return r.bodyToMono(CustomerDto.class)
                .flatMap(this.subscriptionService::createSubscription)
                .flatMap(subscription -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(subscription)))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }


    Mono<ServerResponse> update(ServerRequest r) {
        return r.bodyToMono(SubscriptionDto.class)
                .flatMap(data -> this.subscriptionService.updateSubscription(id(r), data))
                .then(ServerResponse.noContent().build())
                .onErrorResume(this::handleError);
    }


    Mono<ServerResponse> delete(ServerRequest r) {
        return this.subscriptionService.cancelSubscription(id(r))
                .then(ServerResponse.noContent().build())
                .onErrorResume(this::handleError);
    }


    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }

    private Mono<ServerResponse> handleError(Throwable e) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Error message or object"));
    }


}
