package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.SetupIntentDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeCollection;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.SetupIntentUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SetupIntentServiceImpl implements SetupIntentService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;


    public SetupIntentServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    //    List All SetupIntents
    @Override
    public Mono<StripeCollection<SetupIntent>> listSetupIntents() {
//        return Mono.fromCallable(() -> stripeClient.setupIntents()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.setupIntents()
                .list());
    }

    //    Retrieve a SetupIntent
    @Override
    public Mono<SetupIntent> getSetupIntent(String id) {
//        return Mono.fromCallable(() -> stripeClient.setupIntents()
//                        .retrieve(id))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.setupIntents()
                .retrieve(id));
    }

    //    Create a SetupIntent
    @Override
    public Mono<SetupIntent> createSetupIntent(SetupIntentDto setupIntentDto) {
        SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                .setAutomaticPaymentMethods(SetupIntentCreateParams.AutomaticPaymentMethods.builder()
                                                    .setEnabled(true)
                                                    .build())
                .setCustomer(setupIntentDto.getCustomer())
                .setPaymentMethod(setupIntentDto.getPaymentMethod())
                .setDescription(setupIntentDto.getDescription())
                .build();
//        return Mono.fromCallable(() -> stripeClient.setupIntents()
//                        .create(params))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.setupIntents()
                .create(params));
    }

    //    Confirm a SetupIntent
    @Override
    public Mono<Void> confirmSetupIntent(String id) {
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.setupIntents()
//                                .confirm(id);
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
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.setupIntents()
                .confirm(id));
    }

    //    Update a SetupIntent
    @Override
    public Mono<Void> updateSetupIntent(String id, SetupIntentDto setupIntentDto) {
        SetupIntentUpdateParams params = SetupIntentUpdateParams.builder()
                .setCustomer(setupIntentDto.getCustomer())
                .setPaymentMethod(setupIntentDto.getPaymentMethod())
                .setDescription(setupIntentDto.getDescription())
                .build();
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.setupIntents()
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
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.setupIntents()
                .update(id,
                        params));
    }

    //    Cancel a SetupIntent
    @Override
    public Mono<Void> cancelSetupIntent(String id) {
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.setupIntents()
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
    return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.setupIntents()
            .cancel(id));
    }
}
