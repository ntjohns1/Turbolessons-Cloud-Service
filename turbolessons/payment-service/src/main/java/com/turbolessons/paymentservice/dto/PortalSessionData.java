package com.turbolessons.paymentservice.dto;

import lombok.*;

/**
 * Request to open the Stripe Customer Portal for self-service billing
 * management (payment methods, cancellation, invoice history).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PortalSessionData {

    String customer;
    String returnUrl;
}
