package com.noslen.paymentservice.service.price;

import com.noslen.paymentservice.dto.PriceDTO;
import com.noslen.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.Price;
import com.stripe.model.StripeCollection;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PricingServiceImpl implements PricingService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;
    private Mono<Price> standardRateMono;

    public PricingServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
        initializeStandardRate();
    }

    //Initialize the Standard Rate Price Object.

    @Override
    public void initializeStandardRate() {
        PriceCreateParams standardRateParams = PriceCreateParams.builder()
                .setUnitAmount(5000L)
                .setCurrency("usd")
                .setRecurring(PriceCreateParams.Recurring.builder()
                                      .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                      .setIntervalCount(1L)
                                      .setUsageType(PriceCreateParams.Recurring.UsageType.METERED)
                                      .build())
                .setProductData(PriceCreateParams.ProductData.builder()
                                        .setName("Standard Lesson")
                                        .build())
                .setLookupKey("standard_lesson")
                .build();
        this.standardRateMono = createPrice(standardRateParams).cache();
    }

    @Override
    public Mono<Price> getStandardRate() {
        return standardRateMono;
    }

    @Override
    public Mono<StripeCollection<Price>> listAllPrices() {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.prices()
                .list());
    }

    @Override
    public Mono<Price> retrievePrice(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.prices()
                .retrieve(id));
    }

    @Override
    public Mono<Price> createPrice(PriceCreateParams params) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.prices()
                .create(params));
    }

    @Override
    public Mono<Price> createPrice(PriceDTO priceDTO) {
        Boolean recurring = priceDTO.getIsRecurring();
        PriceCreateParams params;
        if (recurring) {
            params = PriceCreateParams.builder()
                    .setUnitAmount(priceDTO.getUnitAmount())
                    .setCurrency(priceDTO.getCurrency())
                    .setRecurring(PriceCreateParams.Recurring.builder()
                                          .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                          .setIntervalCount(1L)
                                          .setUsageType(PriceCreateParams.Recurring.UsageType.METERED)
                                          .build())
                    .setProduct(priceDTO.getProduct())
                    .build();
        } else {
            params = PriceCreateParams.builder()
                    .setUnitAmount(priceDTO.getUnitAmount())
                    .setCurrency(priceDTO.getCurrency())
                    .setProduct(priceDTO.getProduct())
                    .build();
        }

        return stripeClientHelper.executeStripeCall(() -> stripeClient.prices()
                .create(params));
    }

    @Override
    public Mono<Void> updatePrice(String id, PriceDTO priceDTO) {
        PriceUpdateParams params = PriceUpdateParams.builder()
                .setLookupKey(priceDTO.getLookupKey())
                .setActive(priceDTO.getIsActive())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.prices()
                .update(id,
                        params));
    }
}

