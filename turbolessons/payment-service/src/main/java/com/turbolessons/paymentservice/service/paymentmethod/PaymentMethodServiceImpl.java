package com.turbolessons.paymentservice.service.paymentmethod;

import com.turbolessons.paymentservice.dto.BankDto;
import com.turbolessons.paymentservice.dto.CardDto;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public PaymentMethodServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    //    Retrieve a PaymentMethod
    @Override
    public Mono<PaymentMethod> retrievePaymentMethod(String id) {
//
        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentMethods()
                .retrieve(id));
    }

    @Override
    public Mono<StripeCollection<PaymentMethod>> retrieveCustomerPaymentMethods(String customerId) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.customers()
                .paymentMethods()
                .list(customerId));
    }

    @Override
    public Mono<PaymentMethod> createCardPaymentMethod(String customerId, CardDto cardDto) {
        PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(PaymentMethodCreateParams.CardDetails.builder()
                                 .setNumber(cardDto.getNumber())
                                 .setCvc(cardDto.getCvc())
                                 .setExpMonth(cardDto.getExpMonth())
                                 .setExpYear(cardDto.getExpYear())
                                 .build())
                .setCustomer(customerId)
                .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentMethods()
                .create(params));
    }

    @Override
    public Mono<PaymentMethod> createBankPaymentMethod(String customerId, BankDto bankDto) {
        PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.US_BANK_ACCOUNT)
                .setUsBankAccount(PaymentMethodCreateParams.UsBankAccount.builder()
//                                          .setAccountType(PaymentMethodCreateParams.UsBankAccount.AccountType.CHECKING)
                                          .setAccountType(bankDto.getIsChecking() ? PaymentMethodCreateParams.UsBankAccount.AccountType.CHECKING : PaymentMethodCreateParams.UsBankAccount.AccountType.SAVINGS)
                                          .setAccountHolderType(PaymentMethodCreateParams.UsBankAccount.AccountHolderType.INDIVIDUAL)
                                          .setAccountNumber(bankDto.getAccountNumber())
                                          .setRoutingNumber(bankDto.getRoutingNumber())
                                          .build())
                .setCustomer(customerId)
                .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentMethods()
                .create(params));
    }

    @Override
    public Mono<Void> updateCardPaymentMethod(String id, CardDto cardDto) {
        PaymentMethodUpdateParams params = PaymentMethodUpdateParams.builder()
                .setCard(PaymentMethodUpdateParams.Card.builder()
                                 .setExpMonth(cardDto.getExpMonth())
                                 .setExpYear(cardDto.getExpYear())
                                 .build())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.paymentMethods()
                .update(id,
                        params));
    }

    @Override
    public Mono<Void> attachPaymentMethod(String id, String customerId) {
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.paymentMethods()
                .attach(id,
                        params));
    }

    @Override
    public Mono<Void> detachPaymentMethod(String id) {

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.paymentMethods()
                .detach(id));
    }
}
