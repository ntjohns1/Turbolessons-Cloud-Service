package com.turbolessons.paymentservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

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
    private Map<String,String> metadata;
}
