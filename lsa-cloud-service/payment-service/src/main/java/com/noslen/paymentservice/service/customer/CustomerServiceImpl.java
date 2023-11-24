package com.noslen.paymentservice.service.customer;

import com.noslen.paymentservice.dto.Address;
import com.noslen.paymentservice.dto.CustomerDto;
import com.noslen.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public CustomerServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    //     List All Customers
    @Override
    public Mono<StripeCollection<Customer>> listAllCustomers() {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.customers()
                .list());
    }

    //    Retrieve a Customer
    @Override
    public Mono<Customer> retrieveCustomer(String id) {
        return stripeClientHelper.executeStripeCall(() -> this.stripeClient.customers()
                .retrieve(id));
    }

    //    Create a Customer
    @Override
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

        return stripeClientHelper.executeStripeCall(() -> this.stripeClient.customers()
                .create(customerParams));
    }

    //    Update a Customer
    @Override
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

        return stripeClientHelper.executeStripeVoidCall(() -> this.stripeClient.customers()
                .update(id,
                        customerParams));
    }

    //    Delete a Customer
    @Override
    public Mono<Void> deleteCustomer(String id) {
        return stripeClientHelper.executeStripeVoidCall(() -> this.stripeClient.customers()
                .delete(id));
    }
}
