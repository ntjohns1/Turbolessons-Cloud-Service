package com.turbolessons.paymentservice.service.setupintent;

import com.turbolessons.paymentservice.dto.SetupIntentDto;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface SetupIntentService {
    //    List All SetupIntents
    Mono<StripeCollection<SetupIntent>> listSetupIntents();

    //    Retrieve a SetupIntent
    Mono<SetupIntent> retrieveSetupIntent(String id);

    //    Create a SetupIntent
    Mono<SetupIntent> createSetupIntent(SetupIntentDto setupIntentDto);

    //    Confirm a SetupIntent
    Mono<Void> confirmSetupIntent(String id);

    //    Update a SetupIntent
    Mono<Void> updateSetupIntent(String id, SetupIntentDto setupIntentDto);

    //    Cancel a SetupIntent
    Mono<Void> cancelSetupIntent(String id);
}
