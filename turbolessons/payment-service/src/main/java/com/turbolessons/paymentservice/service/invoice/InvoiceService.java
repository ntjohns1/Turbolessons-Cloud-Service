package com.turbolessons.paymentservice.service.invoice;

import com.turbolessons.paymentservice.dto.InvoiceDtoV1;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface InvoiceService {
    Mono<StripeCollection<Invoice>> listAllInvoices();

    Mono<StripeCollection<Invoice>> listAllInvoiceByCustomer(String customerId);

    Mono<StripeCollection<Invoice>> listAllInvoiceBySubscription(String subscriptionId);

    Mono<Invoice> retrieveInvoice(String id);



    Mono<Invoice> createInvoice(InvoiceDtoV1 invoiceDto);

    Mono<Void> updateInvoice(String id, InvoiceDtoV1 invoiceDto);

    Mono<Void> deleteDraftInvoice(String id);

    Mono<Invoice> finalizeInvoice(String id);

    Mono<Invoice> payInvoice(String id);

    Mono<Invoice> voidInvoice(String id);

    Mono<Invoice> markInvoiceUncollectible(String id);

    Mono<StripeCollection<InvoiceLineItem>> getLineItems(String id);
}
