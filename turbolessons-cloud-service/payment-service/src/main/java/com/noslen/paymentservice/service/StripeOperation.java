package com.noslen.paymentservice.service;

import com.stripe.exception.StripeException;

@FunctionalInterface
public interface StripeOperation<T> {
    T execute() throws StripeException;
}

