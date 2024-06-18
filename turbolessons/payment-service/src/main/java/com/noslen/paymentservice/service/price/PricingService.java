package com.noslen.paymentservice.service.price;

import com.noslen.paymentservice.dto.PriceDTO;
import com.stripe.model.Price;
import com.stripe.model.StripeCollection;
import com.stripe.param.PriceCreateParams;
import reactor.core.publisher.Mono;

public interface PricingService {
//    void initializeStandardRate();
//
//    Mono<Price> getStandardRate();

    Mono<StripeCollection<Price>> listAllPrices();

    Mono<Price> retrievePrice(String id);

    Mono<Price> createPrice(PriceCreateParams params);

    Mono<Price> createPrice(PriceDTO priceDTO);

    Mono<Void> updatePrice(String id, PriceDTO priceDTO);
}
