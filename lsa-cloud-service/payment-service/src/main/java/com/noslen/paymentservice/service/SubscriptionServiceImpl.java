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
public class SubscriptionServiceImpl implements SubscriptionService {

    private final StripeClient stripeClient;

    private final StripeClientHelper stripeClientHelper;
    private final CustomerService customerService;
    private final PricingService pricingService;

    public SubscriptionServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper, CustomerService customerService, PricingService pricingService) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
        this.customerService = customerService;
        this.pricingService = pricingService;
    }

    //    List all Subscriptions
    @Override
    public Mono<StripeCollection<Subscription>> listAllSubscriptions() {
//        return Mono.fromCallable(() -> stripeClient.subscriptions()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptions()
                .list());
    }

    //    Retrieve a Subscription by id
    @Override
    public Mono<Subscription> getSubscription(String id) {
//        return Mono.fromCallable(() -> stripeClient.subscriptions()
//                        .retrieve(id))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptions()
                .retrieve(id));
    }

    //    Search Subscriptions by Customer
    @Override
    public Mono<StripeSearchResult<Subscription>> getSubscriptionsByCustomer(String customerId) {
//        return Mono.fromCallable(() -> stripeClient.subscriptions()
//                        .search(SubscriptionSearchParams.builder()
//                                        .setQuery(String.format("customer:%s",
//                                                                customerId))
//                                        .build()))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptions()
                .search(SubscriptionSearchParams.builder()
                                .setQuery(String.format("customer:%s",
                                                        customerId))
                                .build()));
    }

    //    Create a Subscription
    @Override
    public Mono<Subscription> createSubscription(CustomerDto customerDto) {
        return pricingService.getStandardRate()
                .flatMap(standardRate -> customerService.createCustomer(customerDto)
                        .flatMap(customer -> {
                            String customerId = customer.getId();
                            SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                                    .setCustomer(customerId)
                                    .addItem(SubscriptionCreateParams.Item.builder()
                                                     .setPrice(standardRate.getId())
                                                     .build())
                                    .setDefaultPaymentMethod(customerDto.getDefaultPaymentMethod())
                                    .build();
//                            return Mono.fromCallable(() -> stripeClient.subscriptions()
//                                    .create(subscriptionParams));
                            //                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
                            return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptions()
                                    .create(params));
                        }));


    }


    //    Update a Subscription
    @Override
    public Mono<Void> updateSubscription(String id, SubscriptionDto subscriptionDto) {

        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setCancelAt(subscriptionDto.getCancelAt()
                                     .getTime())
                .setCancelAtPeriodEnd(subscriptionDto.getCancelAtPeriodEnd())
                .setDefaultPaymentMethod(subscriptionDto.getDefaultPaymentMethod())
                .build();
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.subscriptions()
//                                .update(id,
//                                        params);
//                    } catch (StripeException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .onErrorMap(ex -> {
//                    if (ex.getCause() instanceof StripeException) {
//                        return new Exception("Error processing Stripe API",
//                                             ex.getCause());
//                    }
//                    return ex;
//                })
//                .then();
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.subscriptions()
                .update(id,
                        params));
    }

    //    Cancel a Subscription
    @Override
    public Mono<Void> cancelSubscription(String id) {

//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.subscriptions()
//                                .cancel(id);
//                    } catch (StripeException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .onErrorMap(ex -> {
//                    if (ex.getCause() instanceof StripeException) {
//                        return new Exception("Error processing Stripe API",
//                                             ex.getCause());
//                    }
//                    return ex;
//                })
//                .then();
        return stripeClientHelper.executeStripeVoidCall(()-> stripeClient.subscriptions()
                .cancel(id));
    }

}
