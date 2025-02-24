package com.turbolessons.paymentservice.service.invoice;
import com.stripe.model.StripeCollection;
import com.turbolessons.paymentservice.dto.InvoiceData;
import com.stripe.model.InvoiceLineItem;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InvoiceService {
    Mono<List<InvoiceData>> listAllInvoices();

    Mono<List<InvoiceData>> listAllInvoiceByCustomer(String customerId);

    Mono<InvoiceData> retrieveUpcomingInvoice(String customerId);

    Mono<List<InvoiceData>> listAllInvoiceBySubscription(String subscriptionId);

    Mono<InvoiceData> retrieveInvoice(String id);

    Mono<InvoiceData> createInvoice(InvoiceData invoiceDto);

    Mono<Void> updateInvoice(String id, InvoiceData invoiceDto);

    Mono<Void> deleteDraftInvoice(String id);

    Mono<InvoiceData> finalizeInvoice(String id);

    Mono<InvoiceData> payInvoice(String id);

    Mono<InvoiceData> voidInvoice(String id);

    Mono<InvoiceData> markInvoiceUncollectible(String id);

    Mono<StripeCollection<InvoiceLineItem>> getLineItems(String id);
}
