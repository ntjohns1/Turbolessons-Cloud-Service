package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class InvoiceDtoV1 {

    String id;

    String currency = "usd";
    String status;
    String customer;
    String subscription;
    String description;
}
