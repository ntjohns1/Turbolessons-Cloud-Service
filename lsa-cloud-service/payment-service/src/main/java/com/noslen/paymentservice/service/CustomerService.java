package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.Address;
import com.noslen.paymentservice.dto.CustomerDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
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

//     List All Customers
    public Mono<StripeCollection<Customer>> listAllCustomers() {
        return Mono.fromCallable(() -> stripeClient.customers()
                        .list())
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    Retrieve a Customer
    public Mono<Customer> getCustomer(String id) {
        return Mono.fromCallable(() -> stripeClient.customers()
                        .retrieve(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    Create a Customer
    public Mono<Customer> createCustomer(CustomerDto customerDto) {
        Address address = customerDto.getAddress();
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setAddress(CustomerCreateParams.Address.builder()
                                    .setCity(address.getCity())
                                    .setCountry("US")
                                    .setLine1(address.getLine1())
                                    .setLine2(address.getLine2())
                                    .setState(address.getState())
                                    .setPostalCode(address.getPostalCode())
                                    .build())
                .setEmail(customerDto.getEmail())
                .setName(customerDto.getName())
                .setPhone(customerDto.getPhone())
                .build();
        return Mono.fromCallable(() -> stripeClient.customers()
                        .create(customerParams))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    Update a Customer
    public Mono<Void> updateCustomer(String id, CustomerDto customerDto) {
        Address address = customerDto.getAddress();
        CustomerUpdateParams customerParams = CustomerUpdateParams.builder()
                .setAddress(CustomerUpdateParams.Address.builder()
                                    .setCity(address.getCity())
                                    .setCountry("US")
                                    .setLine1(address.getLine1())
                                    .setLine2(address.getLine2())
                                    .setState(address.getState())
                                    .setPostalCode(address.getPostalCode())
                                    .build())
                .setEmail(customerDto.getEmail())
                .setName(customerDto.getName())
                .setPhone(customerDto.getPhone())
//                .setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder()
//                                            .setDefaultPaymentMethod(customerDto.getDefaultPaymentMethod())
//                                            .build())
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.customers()
                                .update(id,
                                        customerParams);
                    } catch (StripeException e) {
                        throw new RuntimeException("Error processing Stripe API",
                                                   e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                     }
                    return ex;
                })
                .then();
    }

//    Delete a Customer
    public Mono<Void> deleteCustomer(String id) {
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.customers()
                                .delete(id);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }


}
