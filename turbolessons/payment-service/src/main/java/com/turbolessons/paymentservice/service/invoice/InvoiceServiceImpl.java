package com.turbolessons.paymentservice.service.invoice;

import com.turbolessons.paymentservice.dto.InvoiceDto;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
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
    private final StripeClientHelper stripeClientHelper;

    public InvoiceServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    @Override
    public Mono<StripeCollection<Invoice>> listAllInvoices() {
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .list());
    }

    @Override
    public Mono<StripeCollection<Invoice>> listAllInvoiceByCustomer(String customerId) {

        InvoiceListParams params = InvoiceListParams.builder()
                .setCustomer(customerId)
                .build();
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .list(params));
    }

    @Override
    public Mono<StripeCollection<Invoice>> listAllInvoiceBySubscription(String subscriptionId) {

        InvoiceListParams params = InvoiceListParams.builder()
                .setSubscription(subscriptionId)
                .build();
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .list(params));
    }

    @Override
    public Mono<Invoice> retrieveInvoice(String id) {
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .retrieve(id))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));

        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .retrieve(id));
    }

    @Override
    public Mono<Invoice> createInvoice(InvoiceDto invoiceDto) {
        InvoiceCreateParams params = InvoiceCreateParams.builder()
                .setCustomer(invoiceDto.getCustomer())
                .setSubscription(invoiceDto.getSubscription())
                .setDescription(invoiceDto.getDescription())
                .build();
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .create(params))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                .create(params));
    }

    @Override
    public Mono<Void> updateInvoice(String id, InvoiceDto invoiceDto) {


        InvoiceUpdateParams params = InvoiceUpdateParams.builder()

                .build();
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.invoices()
//                                .update(id,
//                                        params);
//                    } catch (StripeException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .onErrorMap(ex -> {
//                    if (ex.getCause() instanceof StripeException) {
//                        return new Exception("Error processing Stripe API",
//                                             ex.getCause());
//                    }
//                    return ex;
//                })
//                .then();
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.invoices()
                .update(id,
                        params));
    }

    @Override
    public Mono<Void> deleteDraftInvoice(String id) {
//        return Mono.fromRunnable(() -> {
//                    try {
//                        stripeClient.invoices()
//                                .delete(id);
//                    } catch (StripeException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .onErrorMap(ex -> {
//                    if (ex.getCause() instanceof StripeException) {
//                        return new Exception("Error processing Stripe API",
//                                             ex.getCause());
//                    }
//                    return ex;
//                })
//                .then();
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.invoices()
                .delete(id));
    }

    @Override
    public Mono<Invoice> finalizeInvoice(String id) {
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .finalizeInvoice(id))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                .finalizeInvoice(id));
    }

    @Override
    public Mono<Invoice> payInvoice(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                .pay(id));
    }

    @Override
    public Mono<Invoice> voidInvoice(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                .voidInvoice(id));
    }

    @Override
    public Mono<Invoice> markInvoiceUncollectible(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                .markUncollectible(id));
    }

    @Override
    public Mono<StripeCollection<InvoiceLineItem>> getLineItems(String id) {
        return stripeClientHelper.executeStripeCall(() ->stripeClient.invoices()
                .lineItems()
                .list(id));
    }

//    public Mono<Invoice> getUpcomingInvoice(String customerId) {
//        return Mono.fromCallable(() -> stripeClient.invoices().upcoming().)
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
//    }

}
