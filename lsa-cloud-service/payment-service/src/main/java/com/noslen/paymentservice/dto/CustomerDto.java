package com.noslen.paymentservice.dto;

import lombok.*;

import java.io.Serializable;

//@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CustomerDto implements Serializable {

//    @Id
    private String id;
    private Address address;
    private String email;
    private String name;
    private String phone;
    private String defaultPaymentMethod;
    private String description;

}
