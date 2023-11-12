package com.noslen.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CardDto {

    String number;
    String cvc;
    Long expMonth;
    Long expYear;

}
