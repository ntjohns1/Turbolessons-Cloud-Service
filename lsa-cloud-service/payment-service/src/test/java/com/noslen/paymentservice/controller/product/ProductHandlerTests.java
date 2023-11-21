package com.noslen.paymentservice.controller.product;

import com.noslen.paymentservice.controller.customer.CustomerHandlerImpl;
import com.noslen.paymentservice.controller.paymentmethod.PaymentMethodEndpointConfig;
import com.noslen.paymentservice.controller.paymentmethod.PaymentMethodHandler;
import com.noslen.paymentservice.service.paymentmethod.PaymentMethodService;
import com.noslen.paymentservice.service.product.ProductService;
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
@Import(ProductHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class ProductHandlerTests {

    @MockBean
    private ProductService productService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductHandler productHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new ProductEndpointConfig(productHandler).routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldRetrievePrice(ServerRequest r) {

    }

    @Test
    void shouldCreatePrice(ServerRequest r) {

    }

    @Test
    void shouldUpdatePrice(ServerRequest r) {

    }

    @Test
    void shouldDeletePrice(ServerRequest r) {

    }
}
