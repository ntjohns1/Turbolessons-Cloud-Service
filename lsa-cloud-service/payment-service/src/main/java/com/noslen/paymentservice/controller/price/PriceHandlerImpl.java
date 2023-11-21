package com.noslen.paymentservice.controller.price;

import com.noslen.paymentservice.controller.BaseHandler;
import com.noslen.paymentservice.dto.PriceDTO;
import com.stripe.model.Price;
import com.stripe.model.StripeCollection;
import com.stripe.param.PriceCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PriceHandlerImpl extends BaseHandler implements PriceHandler {

    @Override
    public Mono<ServerResponse> getStandardRate(ServerRequest r) {
        return null;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return null;
    }

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
}
