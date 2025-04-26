package com.turbolessons.paymentservice.controller.subscription;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.SubscriptionData;
import com.turbolessons.paymentservice.service.subscription.SubscriptionService;
import com.stripe.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Implementation of the SubscriptionHandler interface.
 */
@Slf4j
@Service
public class SubscriptionHandlerImpl extends BaseHandler implements SubscriptionHandler {

    private final SubscriptionService subscriptionService;

    /**
     * Constructor for SubscriptionHandlerImpl.
     *
     * @param subscriptionService the subscription service
     */
    public SubscriptionHandlerImpl(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Handles the list all subscriptions request.
     *
     * @param r the server request
     * @return a mono of server response
     */
    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                request -> subscriptionService.listAllSubscriptions(),
                new ParameterizedTypeReference<>() {
                });
    }

    /**
     * Retrieves a subscription by ID.
     *
     * @param r the server request
     * @return the server response
     */
    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                request -> subscriptionService.retrieveSubscription(id(request)),
                Subscription.class);
    }

    /**
     * Creates a new subscription.
     *
     * @param r the server request
     * @return the server response
     */
    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                requestBody -> requestBody.flatMap(subscriptionService::createSubscription),
                SubscriptionData.class,
                SubscriptionData.class);
    }

    /**
     * Handles the update subscription request.
     *
     * @param r the server request
     * @return a mono of server response
     */
    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                (idParam, requestBody) -> requestBody.flatMap(dto -> subscriptionService.updateSubscription(idParam, dto)),
                id,
                SubscriptionData.class);
    }

    /**
     * Handles the cancel subscription request.
     *
     * @param r the server request
     * @return a mono of server response
     */
    @Override
    public Mono<ServerResponse> cancel(ServerRequest r) {
        return handleDelete(r,
                request -> subscriptionService.cancelSubscription(id(r)));
    }
}
