package com.noslen.paymentservice.controller.paymentintent;

import com.noslen.paymentservice.controller.customer.CustomerEndpointConfig;
import com.noslen.paymentservice.controller.customer.CustomerHandlerImpl;
import com.noslen.paymentservice.service.customer.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
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

@Log4j2
@WebFluxTest
@Import(PaymentIntentHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class PaymentIntentHandlerTests {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentIntentHandler paymentIntentHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new PaymentIntentEndpointConfig(paymentIntentHandler).routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }


}
