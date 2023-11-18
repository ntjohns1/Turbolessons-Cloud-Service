package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.BankDto;
import com.noslen.paymentservice.dto.CardDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final StripeClient stripeClient;

    public PaymentMethodServiceImpl(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    //    Retrieve a PaymentMethod
    @Override
    public Mono<PaymentMethod> getPaymentMethod(String id) {
        return Mono.fromCallable(() -> stripeClient.paymentMethods()
                        .retrieve(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<StripeCollection<PaymentMethod>> getCustomerPaymentMethods(String customerId) {
        return Mono.fromCallable(() -> stripeClient.customers()
                        .paymentMethods()
                        .list(customerId))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<PaymentMethod> createCardPaymentMethod(String customerId, CardDto cardDto) {
        PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(PaymentMethodCreateParams.CardDetails.builder()
                                 .setNumber(cardDto.getNumber())
                                 .setCvc(cardDto.getCvc())
                                 .setExpMonth(cardDto.getExpMonth())
                                 .setExpYear(cardDto.getExpYear())
                                 .build())
                .setCustomer(customerId)
                .build();
        return Mono.fromCallable(() -> stripeClient.paymentMethods()
                        .create(params))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<PaymentMethod> createBankPaymentMethod(String customerId, BankDto bankDto) {
        PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.US_BANK_ACCOUNT)
                .setUsBankAccount(PaymentMethodCreateParams.UsBankAccount.builder()
//                                          .setAccountType(PaymentMethodCreateParams.UsBankAccount.AccountType.CHECKING)
                                          .setAccountType(bankDto.getIsChecking() ? PaymentMethodCreateParams.UsBankAccount.AccountType.CHECKING : PaymentMethodCreateParams.UsBankAccount.AccountType.SAVINGS)
                                          .setAccountHolderType(PaymentMethodCreateParams.UsBankAccount.AccountHolderType.INDIVIDUAL)
                                          .setAccountNumber(bankDto.getAccountNumber())
                                          .setRoutingNumber(bankDto.getRoutingNumber())
                                          .build())
                .setCustomer(customerId)
                .build();
        return Mono.fromCallable(() -> stripeClient.paymentMethods()
                        .create(params))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Void> updateCardPaymentMethod(String id, CardDto cardDto) {
        PaymentMethodUpdateParams params = PaymentMethodUpdateParams.builder()
                .setCard(PaymentMethodUpdateParams.Card.builder()
                                 .setExpMonth(cardDto.getExpMonth())
                                 .setExpYear(cardDto.getExpYear())
                                 .build())
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.paymentMethods()
                                .update(id,
                                        params);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }

    @Override
    public Mono<Void> attachPaymentMethod(String id, String customerId) {
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.paymentMethods()
                                .attach(id,
                                        params);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }

    @Override
    public Mono<Void> detachPaymentMethod(String id) {
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.paymentMethods()
                                .detach(id);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }
}
