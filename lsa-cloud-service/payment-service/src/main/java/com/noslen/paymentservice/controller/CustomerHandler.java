package com.noslen.paymentservice.controller;

import com.noslen.paymentservice.dto.CustomerDto;
import com.noslen.paymentservice.service.CustomerService;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
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
public class CustomerHandler {

    private final CustomerService customerService;


    public CustomerHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    Mono<ServerResponse> listAll(ServerRequest r) {
        return listAllResponse(this.customerService.listAllCustomers());
    }

    Mono<ServerResponse> retrieve(ServerRequest r) {
        return defaultReadResponse(this.customerService.retrieveCustomer(id(r)));
    }

    Mono<ServerResponse> create(ServerRequest r) {
        Mono<Customer> customerMono = r.bodyToMono(CustomerDto.class)
                .flatMap(this.customerService::createCustomer);

        return defaultWriteResponse(customerMono);
    }

    Mono<ServerResponse> update(ServerRequest r) {
        return r.bodyToMono(CustomerDto.class)
                .flatMap(data -> this.customerService.updateCustomer(id(r),
                                                                     data))
                .then(voidResponse(r))
                .onErrorResume(this::handleError);
    }

    Mono<ServerResponse> delete(ServerRequest r) {
        return this.customerService.deleteCustomer(id(r))
                .then(voidResponse(r))
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> listAllResponse(Publisher<StripeCollection<Customer>> customers) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customers,
                      StripeCollection.class)
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> defaultReadResponse(Publisher<Customer> customer) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customer,
                      Customer.class)
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> defaultWriteResponse(Mono<Customer> customerMono) {
        return customerMono.flatMap(customer -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(customer)))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> voidResponse(ServerRequest r) {
        return ServerResponse.noContent()
                .build()
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
