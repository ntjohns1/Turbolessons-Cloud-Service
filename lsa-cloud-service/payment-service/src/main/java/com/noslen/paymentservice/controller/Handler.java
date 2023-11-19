package com.noslen.paymentservice.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface Handler {
     <T> Mono<ServerResponse> handleListAll(ServerRequest request, ReadRequestProcessor<T> processor, ParameterizedTypeReference<T> typeReference);

    <T> Mono<ServerResponse> handleRetrieve(ServerRequest request, ReadRequestProcessor<T> processor, Class<T> responseBodyClass);

    <T, R> Mono<ServerResponse> handleCreate(ServerRequest request, CreateRequestProcessor<T, R> processor, Class<T> requestBodyClass, Class<R> responseBodyClass);

    <U, T> Mono<ServerResponse> handleUpdate(ServerRequest request, UpdateRequestProcessor<U, T, Void> processor, U additionalParam, Class<T> requestBodyClass);

    <T> Mono<ServerResponse> handleDelete(ServerRequest request, ReadRequestProcessor<T> processor);

    default Mono<ServerResponse> handleError(Throwable e) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Error message or object"));
    }
}
