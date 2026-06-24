package com.turbolessons.paymentservice.service.checkout;

import com.turbolessons.paymentservice.dto.CheckoutSessionData;
import com.turbolessons.paymentservice.dto.PortalSessionData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;
    // The single metered "lessons" price (30-min unit, $30); 1h lessons bill as
    // value 2 via meter events. Overridable per environment.
    private final String defaultLessonPriceId;

    public CheckoutServiceImpl(
            StripeClient stripeClient,
            StripeClientHelper stripeClientHelper,
            @Value("${STRIPE_LESSON_PRICE_ID:price_1PnjXjDmZLVpivSL2QKtD88g}") String defaultLessonPriceId) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
        this.defaultLessonPriceId = defaultLessonPriceId;
    }

    @Override
    public Mono<Session> createCheckoutSession(CheckoutSessionData data) {
        String priceId = (data.getPrice() != null && !data.getPrice().isBlank())
                ? data.getPrice()
                : defaultLessonPriceId;

        // Metered line items must NOT specify a quantity.
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setCustomer(data.getCustomer())
                .addLineItem(SessionCreateParams.LineItem.builder()
                                     .setPrice(priceId)
                                     .build())
                .setSuccessUrl(data.getSuccessUrl())
                .setCancelUrl(data.getCancelUrl())
                .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.checkout()
                .sessions()
                .create(params));
    }

    @Override
    public Mono<com.stripe.model.billingportal.Session> createPortalSession(PortalSessionData data) {
        com.stripe.param.billingportal.SessionCreateParams params =
                com.stripe.param.billingportal.SessionCreateParams.builder()
                        .setCustomer(data.getCustomer())
                        .setReturnUrl(data.getReturnUrl())
                        .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.billingPortal()
                .sessions()
                .create(params));
    }
}
