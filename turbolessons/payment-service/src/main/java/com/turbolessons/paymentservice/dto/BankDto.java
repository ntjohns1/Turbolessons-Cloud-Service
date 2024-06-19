package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BankDto {

    String id;
    String accountNumber;
    String routingNumber;
    Boolean isChecking = true;

}
