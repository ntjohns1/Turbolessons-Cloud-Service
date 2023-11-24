package com.noslen.paymentservice.controller.paymentmethod;

import com.noslen.paymentservice.dto.BankDto;
import com.noslen.paymentservice.dto.CardDto;
import com.noslen.paymentservice.service.paymentmethod.PaymentMethodService;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Log4j2
@WebFluxTest
@Import(PaymentMethodHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class PaymentMethodHandlerTests {


    @MockBean
    private PaymentMethodService paymentMethodService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentMethodHandler paymentMethodHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new PaymentMethodEndpointConfig(paymentMethodHandler).paymentMethodRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldRetrievePaymentMethod() {
        PaymentMethod paymentMethod = createMockCardPaymentMethod("pm_123");
        when(paymentMethodService.retrievePaymentMethod(anyString()))
                     .thenReturn(Mono.just(paymentMethod));

        webTestClient.get()
                .uri("/api/paymentmethod/pm_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(paymentMethod.getId());
    }

    @Test
    void shouldRetrievePaymentMethodByCustomer() {
        StripeCollection<PaymentMethod> paymentMethods = createMockStripeCollection();
        when(paymentMethodService.retrieveCustomerPaymentMethods(anyString()))
                     .thenReturn(Mono.just(paymentMethods));

        webTestClient.get()
                .uri("/api/paymentmethod/customer/cus_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.data")
                .isNotEmpty();
    }

    @Test
    void shouldCreateCardPaymentMethod() {

        PaymentMethod paymentMethod = createMockCardPaymentMethod("cus_123");
        CardDto dto = new CardDto("card_123",
                                  "2424242424242424",
                                  "242",
                                  1L,
                                  2028L);
        when(paymentMethodService.createCardPaymentMethod("cus_123",
                                                          dto))
                     .thenReturn(Mono.just(paymentMethod));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/paymentmethod/card/cus_123")
                .body(Mono.just(dto),
                      CardDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.id")
                .isNotEmpty();
    }

    @Test
    void shouldCreateBankPaymentMethod() {

        PaymentMethod paymentMethod = createMockBankPaymentMethod("cus_123");
        BankDto dto = new BankDto("bank_123",
                                  "123456789012",
                                  "123456789",
                                  true);
        when(paymentMethodService.createBankPaymentMethod("cus_123",
                                                          dto))
                     .thenReturn(Mono.just(paymentMethod));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/paymentmethod/bank/cus_123")
                .body(Mono.just(dto),
                      CardDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.id")
                .isNotEmpty();
    }

    @Test
    void shouldHandleUpdateCardPaymentMethod() {

        PaymentMethod paymentMethod = createMockBankPaymentMethod("cus_123");
        CardDto dto = new CardDto("card_123",
                                  "2424242424242424",
                                  "353",
                                  2L,
                                  2029L);
        when(paymentMethodService.updateCardPaymentMethod(anyString(),any(CardDto.class))).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/paymentmethod/pm_123")
                .body(Mono.just(dto), CardDto.class)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void shouldAttachPaymentMethod() {

        PaymentMethod paymentMethod = createMockBankPaymentMethod("cus_123");

        when(paymentMethodService.attachPaymentMethod(anyString(),anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/paymentmethod/attach/pm_123/cus123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void shouldDetachPaymentMethod() {
        PaymentMethod paymentMethod = createMockBankPaymentMethod("cus_123");

        when(paymentMethodService.detachPaymentMethod(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/paymentmethod/detach/pm_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    private StripeCollection<PaymentMethod> createMockStripeCollection() {
        StripeCollection<PaymentMethod> paymentMethods = new StripeCollection<>();

        PaymentMethod paymentMethod1 = createMockBankPaymentMethod("pm_123");
        PaymentMethod paymentMethod2 = createMockCardPaymentMethod("pm_456");

        List<PaymentMethod> paymentMethodList = Arrays.asList(paymentMethod1,
                                                              paymentMethod2);
        paymentMethods.setData(paymentMethodList);
        return paymentMethods;
    }

    private PaymentMethod createMockCardPaymentMethod(String id) {
        PaymentMethod.BillingDetails billingDetails = Mockito.mock(PaymentMethod.BillingDetails.class);
        when(billingDetails.getEmail()).thenReturn("test@example.com");
        PaymentMethod.Card card = Mockito.mock(PaymentMethod.Card.class);
        PaymentMethod paymentMethod = Mockito.mock(PaymentMethod.class);
        when(paymentMethod.getId()).thenReturn(id);
        when(paymentMethod.getBillingDetails()).thenReturn(billingDetails);
        when(paymentMethod.getCard()).thenReturn(card);
        when(paymentMethod.getType()).thenReturn("card");
        return paymentMethod;
    }

    private PaymentMethod createMockBankPaymentMethod(String id) {
        PaymentMethod.BillingDetails billingDetails = Mockito.mock(PaymentMethod.BillingDetails.class);
        PaymentMethod.UsBankAccount bank = Mockito.mock(PaymentMethod.UsBankAccount.class);
        PaymentMethod paymentMethod = Mockito.mock(PaymentMethod.class);
        when(paymentMethod.getId()).thenReturn(id);
        when(paymentMethod.getBillingDetails()).thenReturn(billingDetails);
        when(paymentMethod.getUsBankAccount()).thenReturn(bank);
        when(paymentMethod.getType()).thenReturn("us_bank_account");
        return paymentMethod;
    }
}
