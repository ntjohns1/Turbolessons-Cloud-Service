package com.turbolessons.paymentservice.service.invoice;

import com.stripe.StripeClient;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.param.*;
import com.turbolessons.paymentservice.dto.InvoiceData;
import com.turbolessons.paymentservice.dto.LineItemData;
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

    private LineItemData mapLineItemData(InvoiceLineItem lineItem) {
        return new LineItemData(lineItem.getId(),
                                lineItem.getAmount(),
                                lineItem.getDescription(),
                                lineItem.getPeriod()
                                        .getStart(),
                                lineItem.getPeriod()
                                        .getEnd(),
                                lineItem.getPrice()
                                        .getId(),
                                lineItem.getInvoice());
    }

    @Override
    public Mono<List<InvoiceData>> listAllInvoices() {
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
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .list(params))
                .map(stripeCollection -> stripeCollection.getData()
                        .stream()
                        .map(this::mapInvoiceData)
                        .toList());
    }

    @Override
    public Mono<InvoiceData> retrieveInvoice(String id) {
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
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .create(params))
                .map(this::mapInvoiceData);
    }

    @Override
    public Mono<Void> updateInvoice(String id, InvoiceData invoiceData) {
        InvoiceUpdateParams params = InvoiceUpdateParams.builder()
                .setDueDate(invoiceData.due_date())
                .setEffectiveAt(invoiceData.effective_at())
                .build();
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.invoices()
                .update(id,
                        params));
    }

    @Override
    public Mono<Void> deleteDraftInvoice(String id) {
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.invoices()
                .delete(id));
    }

    @Override
    public Mono<Void> finalizeInvoice(String id) {
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.invoices()
                .finalizeInvoice(id));
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
    public Mono<List<LineItemData>> getLineItems(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.invoices()
                        .lineItems()
                        .list(id))
                .map(stripeCollection -> stripeCollection.getData()
                        .stream()
                        .map(this::mapLineItemData)
                        .toList());
    }

    @Override
    public Mono<List<LineItemData>> getUpcomingLineItems(String customerId) {
        InvoiceUpcomingLinesParams params = InvoiceUpcomingLinesParams.builder()
                .setCustomer(customerId)
                .build();
        return stripeClientHelper.executeStripeCall(() -> Invoice.upcomingLines(params))
                .map(stripeCollection -> stripeCollection.getData()
                        .stream()
                        .map(this::mapLineItemData)
                        .toList());
    }
}
