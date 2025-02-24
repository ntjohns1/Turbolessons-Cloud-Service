package com.turbolessons.paymentservice.service.invoice;

import com.stripe.StripeClient;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.StripeCollection;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceListParams;
import com.stripe.param.InvoiceUpcomingParams;
import com.stripe.param.InvoiceUpdateParams;
import com.turbolessons.paymentservice.dto.InvoiceData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public InvoiceServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    private InvoiceData mapInvoiceData(Invoice invoice) {
        return new InvoiceData(invoice.getId(),
                               invoice.getAccountName(),
                               invoice.getCustomer(),
                               invoice.getAmountDue(),
                               invoice.getAmountPaid(),
                               invoice.getAmountRemaining(),
                               invoice.getAttemptCount(),
                               invoice.getAttempted(),
                               invoice.getCreated(),
                               invoice.getDueDate(),
                               invoice.getEffectiveAt(),
                               invoice.getEndingBalance(),
                               invoice.getNextPaymentAttempt(),
                               invoice.getPaid());
    }

    @Override
    public Mono<List<InvoiceData>> listAllInvoices() {
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .list())
                .map(stripeCollection -> stripeCollection.getData()
                        .stream()
                        .map(this::mapInvoiceData)
                        .toList());
    }

    @Override
    public Mono<List<InvoiceData>> listAllInvoiceByCustomer(String customerId) {

        InvoiceListParams params = InvoiceListParams.builder()
                .setCustomer(customerId)
                .build();
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .list(params))
                .map(stripeCollection -> stripeCollection.getData()
                        .stream()
                        .map(this::mapInvoiceData)
                        .toList());
    }

    @Override
    public Mono<List<InvoiceData>> listAllInvoiceBySubscription(String subscriptionId) {

        InvoiceListParams params = InvoiceListParams.builder()
                .setSubscription(subscriptionId)
                .build();
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .list())
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .list(params))
                .map(stripeCollection -> stripeCollection.getData()
                        .stream()
                        .map(this::mapInvoiceData)
                        .toList());
    }

    @Override
    public Mono<InvoiceData> retrieveInvoice(String id) {
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .retrieve(id))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));

        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .retrieve(id))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<InvoiceData> retrieveUpcomingInvoice(String customerId) {
        InvoiceUpcomingParams params = InvoiceUpcomingParams.builder()
                .setCustomer(customerId)
                .build();
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .upcoming(params))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<InvoiceData> createInvoice(InvoiceData invoiceData) {
        InvoiceCreateParams params = InvoiceCreateParams.builder()
                .setCustomer(invoiceData.customer())
                .build();
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .create(params))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .create(params))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<Void> updateInvoice(String id, InvoiceData invoiceData) {


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
    public Mono<InvoiceData> finalizeInvoice(String id) {
//        return Mono.fromCallable(() -> stripeClient.invoices()
//                        .finalizeInvoice(id))
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .finalizeInvoice(id))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<InvoiceData> payInvoice(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .pay(id))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<InvoiceData> voidInvoice(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .voidInvoice(id))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<InvoiceData> markInvoiceUncollectible(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .markUncollectible(id))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<StripeCollection<InvoiceLineItem>> getLineItems(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                .lineItems()
                .list(id));
    }

//    public Mono<InvoiceData> getUpcomingInvoice(String customerId) {
//        return Mono.fromCallable(() -> stripeClient.invoices().upcoming().)
//                .onErrorMap(StripeException.class,
//                            e -> new Exception("Error processing Stripe API",
//                                               e));
//    }

}
