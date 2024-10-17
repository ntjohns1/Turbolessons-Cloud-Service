package com.turbolessons.paymentservice.service.customer;

import com.turbolessons.paymentservice.dto.CustomerDto;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface CustomerService {
    //     List All Customers
    Mono<StripeCollection<Customer>> listAllCustomers();

    //    Retrieve a Customer
    Mono<Customer> retrieveCustomer(String id);

    Mono<Customer> searchCustomerBySystemId(String id);

    //    Create a Customer
    Mono<Customer> createCustomer(CustomerDto customerDto);

    //    Update a Customer
    Mono<Void> updateCustomer(String id, CustomerDto customerDto);

    //    Delete a Customer
    Mono<Void> deleteCustomer(String id);
}
