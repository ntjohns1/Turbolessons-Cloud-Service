package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.InvoiceDto;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface InvoiceService {
    Mono<StripeCollection<Invoice>> listAllInvoices();

    Mono<StripeCollection<Invoice>> listAllInvoiceByCustomer(String customerId);

    Mono<StripeCollection<Invoice>> listAllInvoiceBySubscription(String subscriptionId);

    Mono<Invoice> retrieveInvoice(String id);

    Mono<Invoice> createInvoice(InvoiceDto invoiceDto);

    Mono<Void> updateInvoice(String id, InvoiceDto invoiceDto);

    Mono<Void> deleteDraftInvoice(String id);

    Mono<Invoice> finalizeInvoice(String id);

    Mono<Invoice> payInvoice(String id);

    Mono<Invoice> voidInvoice(String id);

    Mono<Invoice> markInvoiceUncollectible(String id);

    Mono<StripeCollection<InvoiceLineItem>> getLineItems(String id);
}
