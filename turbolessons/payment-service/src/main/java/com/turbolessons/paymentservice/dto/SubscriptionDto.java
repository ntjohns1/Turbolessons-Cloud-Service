package com.turbolessons.paymentservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SubscriptionDto implements Serializable {

    private String id;
    private String customer;
    private Boolean cancelAtPeriodEnd;
    private Date cancelAt;
    private String defaultPaymentMethod;

}

