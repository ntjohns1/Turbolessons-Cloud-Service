package com.turbolessons.paymentservice.service.paymentintent;

import com.turbolessons.paymentservice.dto.PaymentIntentDto;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import reactor.core.publisher.Mono;

public interface PaymentIntentService {
    Mono<StripeCollection<PaymentIntent>> listAllPaymentIntents();

    Mono<PaymentIntent> retrievePaymentIntent(String id);

    Mono<StripeSearchResult<PaymentIntent>> searchPaymentIntentByCustomer(String customerId);

    Mono<PaymentIntent> createPaymentIntent(PaymentIntentDto paymentIntentDto);

    Mono<Void> updatePaymentIntent(String id, PaymentIntentDto paymentIntentDto);

    Mono<PaymentIntent> capturePaymentIntent(String id);

    Mono<Void> cancelPaymentIntent(String id);
}
