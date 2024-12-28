package com.turbolessons.paymentservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


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
    private List<String> items;
    private Boolean cancelAtPeriodEnd;
    private Long cancelAt;
    private String defaultPaymentMethod;

}

