package com.turbolessons.paymentservice.controller.customer;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.CustomerDto;
import com.turbolessons.paymentservice.service.customer.CustomerService;
import com.stripe.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CustomerHandlerImpl extends BaseHandler implements CustomerHandler {

    private final CustomerService customerService;

    public CustomerHandlerImpl(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                             request -> this.customerService.listAllCustomers(),
                          new ParameterizedTypeReference<>() {
                             });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> this.customerService.retrieveCustomer(id(request)),
                              Customer.class);
    }

    public Mono<ServerResponse> search(ServerRequest r) {
        return handleRetrieve(r,
                            request -> this.customerService.searchCustomerBySystemId(id(request)), Customer.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.customerService::createCustomer),
                            CustomerDto.class,
                            Customer.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> this.customerService.updateCustomer(idParam, dto)),
                            id,
                            CustomerDto.class);
    }


    @Override
    public Mono<ServerResponse> delete(ServerRequest r) {
        return handleDelete(r,
                            request -> this.customerService.deleteCustomer(id(request)));
    }

}
