package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.CustomerObject;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
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


    public Mono<Subscription> createSubscription(CustomerObject customerObject) {
        return pricingService.getStandardRate()
                .flatMap(standardRate -> customerService.createCustomer(customerObject)
                        .flatMap(customer -> {
                            String customerId = customer.getId();
                            SubscriptionCreateParams subscriptionParams = SubscriptionCreateParams.builder()
                                    .setCustomer(customerId)
                                    .addItem(SubscriptionCreateParams.Item.builder()
                                                     .setPrice(standardRate.getId())
                                                     .build())
                                    .build();
                            return Mono.fromCallable(() -> stripeClient.subscriptions()
                                    .create(subscriptionParams));
                        }))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

}
