package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class InvoiceDto {

    String id;
    String currency = "usd";
    String status;
    String customer;
    String subscription;
    String description;
}
