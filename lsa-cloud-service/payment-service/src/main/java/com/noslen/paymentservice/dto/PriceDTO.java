package com.noslen.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PriceDTO {

//    In cents;
    private String id;
    private Long unitAmount;
    private String currency = "usd";
    private String lookupKey;
    private String product;
    private Boolean isActive = true;
    private Boolean isRecurring = true;
}
