package com.noslen.paymentservice.dao;

import com.stripe.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepo extends ReactiveMongoRepository<Customer, Integer> {
}
