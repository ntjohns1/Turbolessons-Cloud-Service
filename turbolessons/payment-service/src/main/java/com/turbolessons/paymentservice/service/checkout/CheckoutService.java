package com.turbolessons.paymentservice.service.checkout;

import com.turbolessons.paymentservice.dto.CheckoutSessionData;
import com.turbolessons.paymentservice.dto.PortalSessionData;
import reactor.core.publisher.Mono;

public interface CheckoutService {

    Mono<com.stripe.model.checkout.Session> createCheckoutSession(CheckoutSessionData data);

    Mono<com.stripe.model.billingportal.Session> createPortalSession(PortalSessionData data);
}
