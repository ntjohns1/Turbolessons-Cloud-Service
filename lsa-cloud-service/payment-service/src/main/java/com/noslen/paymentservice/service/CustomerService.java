package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.Address;
import com.noslen.paymentservice.dto.CustomerObject;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final StripeClient stripeClient;

    public CustomerService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    public Mono<Customer> createCustomer(CustomerObject customerObject) {
        Address address = customerObject.getAddress();
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setAddress(CustomerCreateParams.Address.builder()
                                    .setCity(address.getCity())
                                    .setCountry("US")
                                    .setLine1(address.getLine1())
                                    .setLine2(address.getLine2())
                                    .setState(address.getState())
                                    .setPostalCode(address.getPostalCode())
                                    .build())
                .setEmail(customerObject.getEmail())
                .setName(customerObject.getName())
                .setPhone(customerObject.getPhone())
                .build();
        return Mono.fromCallable(() -> stripeClient.customers()
                        .create(customerParams))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    public Mono<Void> updateCustomer(CustomerObject customerObject) {
        Address address = customerObject.getAddress();
        CustomerUpdateParams customerParams = CustomerUpdateParams.builder()
                .setAddress(CustomerUpdateParams.Address.builder()
                                    .setCity(address.getCity())
                                    .setCountry("US")
                                    .setLine1(address.getLine1())
                                    .setLine2(address.getLine2())
                                    .setState(address.getState())
                                    .setPostalCode(address.getPostalCode())
                                    .build())
                .setEmail(customerObject.getEmail())
                .setName(customerObject.getName())
                .setPhone(customerObject.getPhone())
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.customers().update(customerObject.getId(), customerParams);
                    } catch (StripeException e) {
                        throw new RuntimeException("Error processing Stripe API", e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API", ex.getCause());
                    }
                    return ex;
                })
                .then();
    }


}
