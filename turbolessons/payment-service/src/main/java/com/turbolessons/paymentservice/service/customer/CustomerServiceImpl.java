package com.turbolessons.paymentservice.service.customer;

import com.stripe.param.CustomerSearchParams;
import com.turbolessons.paymentservice.dto.Address;
import com.turbolessons.paymentservice.dto.CustomerDto;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//You can perform searches on metadata that you’ve added to objects that support it.
//
// metadata search: metadata["<field>"]:"<value>".
//
// queries for records with a donation ID of “asdf-jkl”: metadata["donation-id"]:"asdf-jkl".
//
// You can query for the presence of a metadata key on an object. The following clause would match all records where donation-id is a metadata key. -metadata["donation-id"]:null

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

    @Override
    public Mono<Customer> searchCustomerBySystemId(String id) {
        String query = String.format("metadata['okta_id']:'%s'", id);
        CustomerSearchParams params =
                CustomerSearchParams.builder()
                        .setQuery(query)
                        .build();
        return stripeClientHelper.executeStripeCall(() -> this.stripeClient.customers()
                        .search(params))
                .flatMap(customerSearchResult -> {
                    if (customerSearchResult.getData() != null && !customerSearchResult.getData().isEmpty()) {
                        return Mono.just(customerSearchResult.getData().get(0));
                    } else {
                        return Mono.empty();
                    }
                });    }

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
                .setMetadata(customerDto.getMetadata())
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
