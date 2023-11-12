package com.noslen.paymentservice.service;

import com.stripe.StripeClient;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PricingService {

    private final StripeClient stripeClient;
    private Mono<Price> standardRateMono;

    public PricingService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
        initializeStandardRate();
    }

    private void initializeStandardRate() {
        PriceCreateParams standardRateParams = PriceCreateParams.builder()
                .setUnitAmount(5000L)
                .setCurrency("usd")
                .setProductData(PriceCreateParams.ProductData.builder()
                                        .setName("Standard Lesson")
                                        .build())
                .build();
        this.standardRateMono = createPrice(standardRateParams).cache();
    }

    public Mono<Price> getStandardRate() {
        return standardRateMono;
    }

    private Mono<Price> createPrice(PriceCreateParams params) {
        return Mono.fromCallable(() -> stripeClient.prices().create(params));
    }
}

