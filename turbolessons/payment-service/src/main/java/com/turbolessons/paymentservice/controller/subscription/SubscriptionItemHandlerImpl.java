package com.turbolessons.paymentservice.controller.subscription;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.SubscriptionItemData;
import com.turbolessons.paymentservice.service.subscription.SubscriptionItemService;
import com.stripe.model.SubscriptionItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SubscriptionItemHandlerImpl extends BaseHandler implements SubscriptionItemHandler {

    private final SubscriptionItemService subscriptionItemService;

    public SubscriptionItemHandlerImpl(SubscriptionItemService subscriptionItemService) {
        this.subscriptionItemService = subscriptionItemService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        String subscriptionId = r.pathVariable("subscriptionId");
        log.info("Listing all subscription items for subscription: {}", subscriptionId);
        
        return handleList(r,
                          request -> subscriptionItemService.listSubscriptionItems(subscriptionId)
                                .doOnSuccess(items -> log.info("Successfully retrieved subscription items for subscription: {}", subscriptionId))
                                .doOnError(error -> log.error("Error retrieving subscription items for subscription {}: {}", 
                                                            subscriptionId, error.getMessage(), error)),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        String id = id(r);
        log.info("Retrieving subscription item: {}", id);
        
        return handleRetrieve(r,
                              request -> subscriptionItemService.retrieveSubscriptionItem(id)
                                    .doOnSuccess(item -> log.info("Successfully retrieved subscription item: {}", id))
                                    .doOnError(error -> log.error("Error retrieving subscription item {}: {}", 
                                                                id, error.getMessage(), error)),
                              SubscriptionItem.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        String subscriptionId = r.pathVariable("subscriptionId");
        log.info("Creating subscription item for subscription: {}", subscriptionId);
        
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(dto -> {
                                log.info("Received request to create subscription item with price: {}, quantity: {}", 
                                         dto.price(), dto.quantity());
                                
                                // Ensure the subscription ID from the path is used
                                SubscriptionItemData itemWithSubscription = new SubscriptionItemData(
                                    dto.id(),
                                    subscriptionId, // Use the path parameter
                                    dto.price(),
                                    dto.quantity(),
                                    dto.priceData(),
                                    dto.proration_date(),
                                    dto.tax_rates(),
                                    dto.deleted(),
                                    dto.metadata()
                                );
                                
                                return subscriptionItemService.createSubscriptionItem(itemWithSubscription)
                                    .doOnSuccess(item -> log.info("Successfully created subscription item: {} for subscription: {}", 
                                                                  item.id(), item.subscription()))
                                    .doOnError(error -> log.error("Error creating subscription item for subscription {}: {}", 
                                                                 subscriptionId, error.getMessage(), error));
                            }),
                            SubscriptionItemData.class,
                            SubscriptionItemData.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        log.info("Updating subscription item: {}", id);
        
        return handleUpdate(r,
                            ((idParam, requestBody) -> requestBody.flatMap(dto -> {
                                log.info("Received request to update subscription item {} with price: {}, quantity: {}", 
                                        idParam, dto.price(), dto.quantity());
                                
                                return subscriptionItemService.updateSubscriptionItem(idParam, dto)
                                    .doOnSuccess(result -> log.info("Successfully updated subscription item: {}", idParam))
                                    .doOnError(error -> log.error("Error updating subscription item {}: {}", 
                                                                idParam, error.getMessage(), error));
                            })),
                            id,
                            SubscriptionItemData.class);
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest r) {
        String id = id(r);
        log.info("Deleting subscription item: {}", id);
        
        return handleDelete(r,
                            request -> subscriptionItemService.deleteSubscriptionItem(id)
                                .doOnSuccess(result -> log.info("Successfully deleted subscription item: {}", id))
                                .doOnError(error -> log.error("Error deleting subscription item {}: {}", 
                                                            id, error.getMessage(), error)));
    }
}
