package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.CustomerDto;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface CustomerService {
    //     List All Customers
    Mono<StripeCollection<Customer>> listAllCustomers();

    //    Retrieve a Customer
    Mono<Customer> retrieveCustomer(String id);

    //    Create a Customer
    Mono<Customer> createCustomer(CustomerDto customerDto);

    //    Update a Customer
    Mono<Void> updateCustomer(String id, CustomerDto customerDto);

    //    Delete a Customer
    Mono<Void> deleteCustomer(String id);
}
