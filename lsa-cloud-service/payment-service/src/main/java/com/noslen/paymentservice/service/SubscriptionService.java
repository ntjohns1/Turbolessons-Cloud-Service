package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.CustomerDto;
import com.noslen.paymentservice.dto.SubscriptionDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionSearchParams;
import com.stripe.param.SubscriptionUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SubscriptionService {

    private final StripeClient stripeClient;
    private final CustomerService customerService;
    private final PricingService pricingService;

    public SubscriptionService(StripeClient stripeClient, CustomerService customerService, PricingService pricingService) {
        this.stripeClient = stripeClient;
        this.customerService = customerService;
        this.pricingService = pricingService;
    }

//    List all Subscriptions
    public Mono<StripeCollection<Subscription>> listAllSubscriptions() {
        return Mono.fromCallable(() -> stripeClient.subscriptions()
                        .list())
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    Retrieve a Subscription by id
    public Mono<Subscription> getSubscription(String id) {
        return Mono.fromCallable(() -> stripeClient.subscriptions()
                        .retrieve(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    Search Subscriptions by Customer
    public Mono<StripeSearchResult<Subscription>> getSubscriptionsByCustomer(String customerId) {
        return Mono.fromCallable(() -> stripeClient.subscriptions()
                        .search(SubscriptionSearchParams.builder()
                                        .setQuery(String.format("customer:%s",
                                                                customerId))
                                        .build()))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    Create a Subscription
    public Mono<Subscription> createSubscription(CustomerDto customerDto) {
        return pricingService.getStandardRate()
                .flatMap(standardRate -> customerService.createCustomer(customerDto)
                        .flatMap(customer -> {
                            String customerId = customer.getId();
                            SubscriptionCreateParams subscriptionParams = SubscriptionCreateParams.builder()
                                    .setCustomer(customerId)
                                    .addItem(SubscriptionCreateParams.Item.builder()
                                                     .setPrice(standardRate.getId())
                                                     .build())
                                    .setDefaultPaymentMethod(customerDto.getDefaultPaymentMethod())
                                    .build();
                            return Mono.fromCallable(() -> stripeClient.subscriptions()
                                    .create(subscriptionParams));
                        }))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    Update a Subscription
    public Mono<Void> updateSubscription(String id, SubscriptionDto subscriptionDto) {

        SubscriptionUpdateParams subscriptionParams = SubscriptionUpdateParams.builder()
                .setCancelAt(subscriptionDto.getCancelAt()
                                     .getTime())
                .setCancelAtPeriodEnd(subscriptionDto.getCancelAtPeriodEnd())
                .setDefaultPaymentMethod(subscriptionDto.getDefaultPaymentMethod())
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.subscriptions()
                                .update(id,
                                        subscriptionParams);
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

//    Cancel a Subscription
    public Mono<Void> cancelSubscription(String id) {

        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.subscriptions()
                                .cancel(id);
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
