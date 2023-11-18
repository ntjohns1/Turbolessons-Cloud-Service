package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.InvoiceDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.StripeCollection;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceListParams;
import com.stripe.param.InvoiceUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final StripeClient stripeClient;

    public InvoiceServiceImpl(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @Override
    public Mono<StripeCollection<Invoice>> listAllInvoices() {
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .list())
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<StripeCollection<Invoice>> listAllInvoiceByCustomer(String customerId) {

        InvoiceListParams params = InvoiceListParams.builder()
                .setCustomer(customerId)
                .build();
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .list())
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<StripeCollection<Invoice>> listAllInvoiceBySubscription(String subscriptionId) {

        InvoiceListParams params = InvoiceListParams.builder()
                .setSubscription(subscriptionId)
                .build();
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .list())
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Invoice> retrieveInvoice(String id) {
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .retrieve(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Invoice> createInvoice(InvoiceDto invoiceDto) {
        InvoiceCreateParams params = InvoiceCreateParams.builder()
                .setCustomer(invoiceDto.getCustomer())
                .setSubscription(invoiceDto.getSubscription())
                .setDescription(invoiceDto.getDescription())
                .build();
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .create(params))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Void> updateInvoice(String id, InvoiceDto invoiceDto) {
        InvoiceUpdateParams params = InvoiceUpdateParams.builder()
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.invoices()
                                .update(id,
                                        params);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }

    @Override
    public Mono<Void> deleteDraftInvoice(String id) {
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.invoices()
                                .delete(id);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }

    @Override
    public Mono<Invoice> finalizeInvoice(String id) {
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .finalizeInvoice(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Invoice> payInvoice(String id) {
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .pay(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Invoice> voidInvoice(String id) {
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .voidInvoice(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Invoice> markInvoiceUncollectible(String id) {
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .markUncollectible(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<StripeCollection<InvoiceLineItem>> getLineItems(String id) {
        return Mono.fromCallable(() -> stripeClient.invoices()
                        .lineItems()
                        .list(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

//    public Mono<Invoice> getUpcomingInvoice(String customerId) {
//        return Mono.fromCallable(() -> stripeClient.invoices().upcoming().)
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
//    }

}
