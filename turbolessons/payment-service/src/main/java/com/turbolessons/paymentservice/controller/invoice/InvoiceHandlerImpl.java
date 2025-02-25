package com.turbolessons.paymentservice.controller.invoice;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.InvoiceData;
import com.turbolessons.paymentservice.service.invoice.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class InvoiceHandlerImpl extends BaseHandler implements InvoiceHandler {

    private final InvoiceService invoiceService;

    public InvoiceHandlerImpl(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                          request -> invoiceService.listAllInvoices(),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> listAllByCustomer(ServerRequest r) {
        return handleList(r,
                          request -> invoiceService.listAllInvoiceByCustomer(id(request)),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> listAllBySubscription(ServerRequest r) {
        return handleList(r,
                          request -> invoiceService.listAllInvoiceBySubscription(id(request)),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> invoiceService.retrieveInvoice(id(request)),
                              InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> retrieveUpcoming(ServerRequest r) {
        return handleRetrieve(r,
                              request -> invoiceService.retrieveUpcomingInvoice(id(request)),
                              InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.invoiceService::createInvoice),
                            InvoiceData.class,
                            InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> invoiceService.updateInvoice(idParam,
                                                                                                              dto)),
                            id,
                            InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> deleteDraft(ServerRequest r) {
        return handleDelete(r,
                            request -> invoiceService.deleteDraftInvoice(id(request)));
    }

    @Override
    public Mono<ServerResponse> finalize(ServerRequest r) {
        return invoiceService.finalizeInvoice(id(r))
                .then(ServerResponse.noContent()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> payInvoice(ServerRequest r) {
        return invoiceService.payInvoice(id(r))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> voidInvoice(ServerRequest r) {
        return invoiceService.voidInvoice(id(r))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> markUncollectible(ServerRequest r) {
        return invoiceService.markInvoiceUncollectible(id(r))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> retrieveLineItems(ServerRequest r) {
        return handleSearch(r,
                            request -> this.invoiceService.getLineItems(id(request)),
                            new ParameterizedTypeReference<>() {
                            });
    }

    @Override
    public Mono<ServerResponse> retrieveUpcomingLineItems(ServerRequest r) {
        return handleSearch(r,
                            request -> this.invoiceService.getUpcomingLineItems(id(request)),
                            new ParameterizedTypeReference<>() {
                            });
    }

}
