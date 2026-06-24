package com.turbolessons.paymentservice.controller.checkout;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface CheckoutHandler {

    Mono<ServerResponse> createCheckoutSession(ServerRequest r);

    Mono<ServerResponse> createPortalSession(ServerRequest r);
}
