package com.noslen.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SetupIntentDto {

    String id;
    String customer;
    String paymentMethod;
    String description;
}
