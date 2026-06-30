package com.turbolessons.paymentservice.controller.checkout;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.CheckoutSessionData;
import com.turbolessons.paymentservice.dto.PortalSessionData;
import com.turbolessons.paymentservice.service.checkout.CheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CheckoutHandlerImpl extends BaseHandler implements CheckoutHandler {

    private final CheckoutService checkoutService;

    public CheckoutHandlerImpl(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    //    Create a Checkout Session (subscription enrollment). Returns the Session
    //    whose `url` the SPA redirects to.
    @Override
    public Mono<ServerResponse> createCheckoutSession(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.checkoutService::createCheckoutSession),
                            CheckoutSessionData.class,
                            com.stripe.model.checkout.Session.class);
    }

    //    Create a Customer Portal Session. Returns the Session whose `url` the
    //    SPA redirects to for self-service billing management.
    @Override
    public Mono<ServerResponse> createPortalSession(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.checkoutService::createPortalSession),
                            PortalSessionData.class,
                            com.stripe.model.billingportal.Session.class);
    }
}
