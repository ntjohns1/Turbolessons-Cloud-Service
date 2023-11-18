package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.CustomerDto;
import com.noslen.paymentservice.dto.SubscriptionDto;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.Subscription;
import reactor.core.publisher.Mono;

public interface SubscriptionService {
    //    List all Subscriptions
    Mono<StripeCollection<Subscription>> listAllSubscriptions();

    //    Retrieve a Subscription by id
    Mono<Subscription> getSubscription(String id);

    //    Search Subscriptions by Customer
    Mono<StripeSearchResult<Subscription>> getSubscriptionsByCustomer(String customerId);

    //    Create a Subscription
    Mono<Subscription> createSubscription(CustomerDto customerDto);

    //    Update a Subscription
    Mono<Void> updateSubscription(String id, SubscriptionDto subscriptionDto);

    //    Cancel a Subscription
    Mono<Void> cancelSubscription(String id);
}
