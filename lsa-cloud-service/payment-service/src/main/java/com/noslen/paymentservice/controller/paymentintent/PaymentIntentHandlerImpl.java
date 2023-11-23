package com.noslen.paymentservice.controller.paymentintent;

import com.noslen.paymentservice.controller.BaseHandler;
import com.noslen.paymentservice.dto.PaymentIntentDto;
import com.noslen.paymentservice.service.paymentintent.PaymentIntentService;
import com.stripe.model.PaymentIntent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class PaymentIntentHandlerImpl extends BaseHandler implements PaymentIntentHandler {

    private final PaymentIntentService paymentIntentService;

    public PaymentIntentHandlerImpl(PaymentIntentService paymentIntentService) {
        this.paymentIntentService = paymentIntentService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                             request -> paymentIntentService.listAllPaymentIntents(),
                          new ParameterizedTypeReference<>() {
                             });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {

        return handleRetrieve(r,
                              request -> paymentIntentService.retrievePaymentIntent(id(request)),
                              PaymentIntent.class);
    }

    @Override
    public Mono<ServerResponse> searchByCustomer(ServerRequest r) {
        return handleSearch(r,
                            request -> paymentIntentService.searchPaymentIntentByCustomer(id(request)),
                            new ParameterizedTypeReference<>() {
                            });
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(paymentIntentService::createPaymentIntent),
                            PaymentIntentDto.class,
                            PaymentIntent.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> paymentIntentService.updatePaymentIntent(idParam,
                                                                                                                          dto)),
                            id,
                            PaymentIntentDto.class);
    }

    @Override
    public Mono<ServerResponse> capture(ServerRequest r) {
        return handleCapture(r,
                             request -> paymentIntentService.capturePaymentIntent(id(request)),
                             PaymentIntent.class);
    }

    @Override
    public Mono<ServerResponse> cancel(ServerRequest r) {
        return handleDelete(r,
                            request -> paymentIntentService.cancelPaymentIntent(id(request)));
    }
}
