package com.noslen.paymentservice.service;

import reactor.core.publisher.Mono;

public interface StripeClientHelper {
    <T> Mono<T> executeStripeCall(StripeOperation<T> stripeOperation);

    Mono<Void> executeStripeVoidCall(StripeVoidOperation stripeOperation);
}
