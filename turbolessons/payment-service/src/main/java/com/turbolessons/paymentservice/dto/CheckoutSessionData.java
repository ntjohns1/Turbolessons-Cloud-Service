package com.turbolessons.paymentservice.dto;

import lombok.*;

/**
 * Request to start a metered-subscription enrollment via Stripe Checkout.
 * `price` is optional — defaults to the configured lesson price when blank.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CheckoutSessionData {

    String customer;
    String price;
    String successUrl;
    String cancelUrl;
}
