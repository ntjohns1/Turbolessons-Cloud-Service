package com.turbolessons.paymentservice.service.subscription;

import com.stripe.StripeClient;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionItem;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionSearchParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.turbolessons.paymentservice.dto.SubscriptionDto;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.turbolessons.paymentservice.service.customer.CustomerService;
import com.turbolessons.paymentservice.service.price.PricingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    private SubscriptionDto mapSubscriptionToDto(Subscription subscription) {
        List<String> items = new ArrayList<>();
        if (subscription.getItems() != null && subscription.getItems()
                .getData() != null) {
            for (SubscriptionItem item : subscription.getItems()
                    .getData()) {
                items.add(item.getId());
            }
        }
        return new SubscriptionDto(subscription.getId(),
                                   subscription.getCustomer(),
                                   items,
                                   subscription.getCancelAtPeriodEnd(),
                                   subscription.getCanceledAt(),
                                   subscription.getDefaultPaymentMethod());
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
    public Mono<Subscription> retrieveSubscription(String id) {
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
//    @Override
//    public Mono<Subscription> createSubscription(String id, CustomerDto customerDto) {
//        return customerService.createCustomer(customerDto)
//                .flatMap(customer -> {
//                    String customerId = customer.getId();
//                    SubscriptionCreateParams params = SubscriptionCreateParams.builder()
//                            .setCustomer(customerId)
//                            .addItem(SubscriptionCreateParams.Item.builder()
//                                             .setPrice(id)
//                                             .build())
//                            .setDefaultPaymentMethod(customerDto.getDefaultPaymentMethod())
//                            .build();
////                            return Mono.fromCallable(() -> stripeClient.subscriptions()
////                                    .create(subscriptionParams));
//                    //                .onErrorMap(StripeException.class,
////                            e -> new Exception("Error processing Stripe API",
////                                               e));
//                    return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptions()
//                            .create(params));
//                });
//    }
    @Override
    public Mono<SubscriptionDto> createSubscription(SubscriptionDto subscriptionDto) {

        SubscriptionCreateParams.Builder paramsBuilder = SubscriptionCreateParams.builder()
                .setCustomer(subscriptionDto.getCustomer())
                .setCancelAtPeriodEnd(subscriptionDto.getCancelAtPeriodEnd())
                .setCancelAt(subscriptionDto.getCancelAt())
                .setDefaultPaymentMethod(subscriptionDto.getDefaultPaymentMethod());

        subscriptionDto.getItems()
                .forEach(priceId -> paramsBuilder.addItem(SubscriptionCreateParams.Item.builder()
                                                                  .setPrice(priceId)
                                                                  .build()));
        SubscriptionCreateParams params = paramsBuilder.build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptions()
                        .create(params))
                .map(this::mapSubscriptionToDto);

    }


    //    Update a Subscription
    @Override
    public Mono<Void> updateSubscription(String id, SubscriptionDto subscriptionDto) {

        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setCancelAt(subscriptionDto.getCancelAt())
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
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.subscriptions()
                .cancel(id));
    }

}
