package com.turbolessons.paymentservice.controller.price;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.PriceData;
import com.turbolessons.paymentservice.service.price.PricingService;
import com.stripe.model.Price;
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

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                          request -> pricingService.listAllPrices(),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> pricingService.retrievePrice(id(request)),
                              Price.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(pricingService::createPrice),
                            PriceData.class,
                            Price.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            ((idParam, requestBody) -> requestBody.flatMap(dto -> pricingService.updatePrice(idParam, dto))),
                            id,
                            PriceData.class);
    }
}
