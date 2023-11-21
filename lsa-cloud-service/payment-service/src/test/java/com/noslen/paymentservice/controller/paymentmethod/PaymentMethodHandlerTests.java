package com.noslen.paymentservice.controller.paymentmethod;

import com.noslen.paymentservice.controller.customer.CustomerHandlerImpl;
import com.noslen.paymentservice.controller.paymentintent.PaymentIntentEndpointConfig;
import com.noslen.paymentservice.controller.paymentintent.PaymentIntentHandler;
import com.noslen.paymentservice.service.paymentintent.PaymentIntentService;
import com.noslen.paymentservice.service.paymentmethod.PaymentMethodService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
        RouterFunction<ServerResponse> routerFunction = new PaymentMethodEndpointConfig(paymentMethodHandler).routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldRetrievePaymentMethod(ServerRequest r) {

    }

    @Test
    void shouldRetrievePaymentMethodByCustomer(ServerRequest r) {

    }
    @Test
    void shouldCreateCardPaymentMethod(ServerRequest r) {

    }
    @Test
    void shouldCreateBankPaymentMethod(ServerRequest r) {

    }
    @Test
    void shouldUpdateCardPaymentMethod(ServerRequest r) {

    }
    @Test
    void shouldAttachPaymentMethod(ServerRequest r) {

    }
    @Test
    void shouldDetachPaymentMethod(ServerRequest r) {

    }
}
