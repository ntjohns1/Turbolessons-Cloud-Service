package com.noslen.paymentservice.controller.price;

import com.noslen.paymentservice.controller.BaseHandler;
import com.noslen.paymentservice.dto.PriceDTO;
import com.noslen.paymentservice.service.price.PricingService;
import com.stripe.model.Price;
import com.stripe.model.StripeCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PriceHandlerImpl extends BaseHandler implements PriceHandler {

    private final PricingService pricingService;

    public PriceHandlerImpl(PricingService pricingService) {
        this.pricingService = pricingService;
    }

//    @Override
//    public Mono<ServerResponse> getStandardRate(ServerRequest r) {
//
//        return handleRetrieve(r,
//                              request -> this.pricingService.getStandardRate(),
//                              Price.class);
//    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {

        return handleList(r,
                          request -> this.pricingService.listAllPrices(),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {

        return handleRetrieve(r,
                              request -> this.pricingService.retrievePrice(id(request)),
                              Price.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {

        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.pricingService::createPrice),
                            PriceDTO.class,
                            Price.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> this.pricingService.updatePrice(idParam,dto)),
                            id,
                            PriceDTO.class);
    }
}
