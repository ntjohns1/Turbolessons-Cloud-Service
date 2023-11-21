package com.noslen.paymentservice.controller.setupintent;

import com.noslen.paymentservice.controller.BaseHandler;
import com.noslen.paymentservice.dto.SetupIntentDto;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SetupIntentHandlerImpl extends BaseHandler implements SetupIntentHandler {

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return null;
    }

    //    Retrieve a SetupIntent
    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return null;
    }

    //    Create a SetupIntent
    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return null;
    }

    //    Confirm a SetupIntent
    @Override
    public Mono<ServerResponse> confirm(ServerRequest r) {
        return null;
    }

    //    Update a SetupIntent
    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        return null;
    }

    //    Cancel a SetupIntent
    @Override
    public Mono<ServerResponse> cancel(ServerRequest r) {
        return null;
    }
}
