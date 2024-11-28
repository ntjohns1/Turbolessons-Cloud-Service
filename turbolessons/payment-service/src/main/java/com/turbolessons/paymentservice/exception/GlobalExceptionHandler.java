package com.turbolessons.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public Mono<ServerResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                                             ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ServerResponse> handleGeneralException(Exception ex) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             "An unexpected error occurred"));
    }

    // DTO for error response
    public static class ErrorResponse {
        private final int status;
        private final String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}