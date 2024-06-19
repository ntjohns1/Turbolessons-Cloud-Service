package com.turbolessons.paymentservice.service.paymentmethod;

import com.turbolessons.paymentservice.dto.BankDto;
import com.turbolessons.paymentservice.dto.CardDto;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface PaymentMethodService {
    //    Retrieve a PaymentMethod
    Mono<PaymentMethod> retrievePaymentMethod(String id);

    Mono<StripeCollection<PaymentMethod>> retrieveCustomerPaymentMethods(String customerId);

    Mono<PaymentMethod> createCardPaymentMethod(String customerId, CardDto cardDto);

    Mono<PaymentMethod> createBankPaymentMethod(String customerId, BankDto bankDto);

    Mono<Void> updateCardPaymentMethod(String id, CardDto cardDto);

    Mono<Void> attachPaymentMethod(String id, String customerId);

    Mono<Void> detachPaymentMethod(String id);
}
