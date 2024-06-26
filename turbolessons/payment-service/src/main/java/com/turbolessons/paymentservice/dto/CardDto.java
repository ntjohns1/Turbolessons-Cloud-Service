package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CardDto {

    String id;
    String number;
    String cvc;
    Long expMonth;
    Long expYear;

}
