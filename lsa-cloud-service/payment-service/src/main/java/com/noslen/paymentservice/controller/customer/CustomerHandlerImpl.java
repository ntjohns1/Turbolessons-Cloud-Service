package com.noslen.paymentservice.controller.customer;

import com.noslen.paymentservice.controller.BaseHandler;
import com.noslen.paymentservice.dto.CustomerDto;
import com.noslen.paymentservice.service.customer.CustomerService;
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
        return handleListAll(r,
                             request -> customerService.listAllCustomers(),
                             new ParameterizedTypeReference<>() {
                             });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> customerService.retrieveCustomer(id(request)),
                              Customer.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(customerService::createCustomer),
                            CustomerDto.class,
                            Customer.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> customerService.updateCustomer(idParam,
                                                                                                                dto)),
                            id,
                            CustomerDto.class);
    }


    @Override
    public Mono<ServerResponse> delete(ServerRequest r) {
        return handleDelete(r,
                            request -> customerService.deleteCustomer(id(request)));
    }

}
