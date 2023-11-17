package com.noslen.paymentservice.controller;

import com.noslen.paymentservice.service.CustomerService;
import com.stripe.StripeClient;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;
import static reactor.core.publisher.Mono.when;

@Log4j2
@WebFluxTest
@Import(CustomerHandler.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class CustomerHandlerTests {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerHandler customerHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new CustomerEndpointConfig(customerHandler).routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    public void shouldHandleRequestsForListAllCustomers() {
        StripeCollection<Customer> mockCustomers = createMockStripeCollection();
        Mockito.when(customerService.listAllCustomers()).thenReturn(Mono.just(mockCustomers));

        webTestClient.get().uri("/api/customer")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.data")
                .isNotEmpty();
    }

    // Helper method to create mock StripeCollection
    private StripeCollection<Customer> createMockStripeCollection() {
        StripeCollection<Customer> customers = new StripeCollection<>();

        Customer mockCustomer1 = Mockito.mock(Customer.class);
        Mockito.when(mockCustomer1.getId()).thenReturn("cus_123");
        // Set other necessary properties of mockCustomer1

        Customer mockCustomer2 = Mockito.mock(Customer.class);
        Mockito.when(mockCustomer2.getId()).thenReturn("cus_456");
        // Set other necessary properties of mockCustomer2

        List<Customer> customerList = Arrays.asList(mockCustomer1, mockCustomer2);
        customers.setData(customerList);
        return customers;
    }

}

