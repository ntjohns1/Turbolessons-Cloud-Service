package com.noslen.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BankDto {

    String accountNumber;
    String routingNumber;
    Boolean isChecking = true;

}
