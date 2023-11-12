package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.SetupIntentDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeCollection;
import com.stripe.param.SetupIntentConfirmParams;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.SetupIntentUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SetupIntentService {

    private final StripeClient stripeClient;


    public SetupIntentService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    //    List All SetupIntents
    public Mono<StripeCollection<SetupIntent>> listSetupIntents() {
        return Mono.fromCallable(() -> stripeClient.setupIntents()
                .list());
    }

    //    Retrieve a SetupIntent
    public Mono<SetupIntent> getSetupIntent(String id) {
        return Mono.fromCallable(() -> stripeClient.setupIntents()
                .retrieve(id));
    }

    //    Create a SetupIntent
    public Mono<SetupIntent> createSetupIntent(SetupIntentDto setupIntentDto) {
        SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                .build();
        return Mono.fromCallable(() -> stripeClient.setupIntents()
                        .create(params))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    //    Confirm a SetupIntent
    public Mono<Void> confirmSetupIntent(String id) {
        SetupIntentConfirmParams params = SetupIntentConfirmParams.builder()
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.setupIntents()
                                .confirm(id);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e))
                .then();
    }

//    Update a SetupIntent
    public Mono<Void> updateSetupIntent(String id, SetupIntentDto setupIntentDto) {
        SetupIntentUpdateParams params = SetupIntentUpdateParams.builder()
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.setupIntents()
                                .update(id,params);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e))
                .then();
    }
//    Cancel a SetupIntent
    public Mono<Void> cancelSetupIntent(String id) {
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.setupIntents()
                                .cancel(id);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e))
                .then();
    }
}
