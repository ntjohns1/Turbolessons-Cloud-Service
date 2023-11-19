package com.noslen.paymentservice.controller;

import com.noslen.paymentservice.dto.Address;
import com.noslen.paymentservice.dto.CustomerDto;
import com.noslen.paymentservice.service.CustomerService;
import com.stripe.model.Customer;
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
@Import(CustomerHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class CustomerHandlerTests {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerHandlerImpl customerHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new CustomerEndpointConfig(customerHandler).routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    public void shouldHandleListAllCustomers() {
        StripeCollection<Customer> mockCustomers = createMockStripeCollection();
        when(customerService.listAllCustomers()).thenReturn(Mono.just(mockCustomers));
        System.out.println(mockCustomers);
        webTestClient.get()
                .uri("/api/customer")
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
    void shouldHandleRetrieveCustomer() {
        Customer customer = createMockCustomer();
        when(customerService.retrieveCustomer(anyString())).thenReturn(Mono.just(customer));
        webTestClient.get()
                .uri("/api/customer/cus_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("cus_123")
                .jsonPath("$.email")
                .isEqualTo("test@example.com")
                .jsonPath("$.name")
                .isEqualTo("Andrew Anderson");
    }

    @Test
    void shouldHandleCreateCustomer() {
        Customer customer = createMockCustomer();
        Address address = new Address();
        address.setCity("Los Angeles");
        address.setState("CA");
        address.setCountry("US");
        address.setLine1("123 Easy St.");
        address.setLine2("APT A");
        address.setPostalCode("12345");

        CustomerDto data = new CustomerDto("cus_123",
                                           address,
                                           "email@example.com",
                                           "Claudia Coulthard",
                                           "1234567890",
                                           "pm_789",
                                           "Test DTO");
        when(customerService.createCustomer(any())).thenReturn(Mono.just(customer));
        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/customer")
                .body(Mono.just(data),
                      CustomerDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("cus_123")
                .jsonPath("$.email")
                .isEqualTo("test@example.com")
                .jsonPath("$.name")
                .isEqualTo("Andrew Anderson");
    }

    @Test
    void shouldHandleUpdateCustomer() {
        Customer customer = createMockCustomer();
        CustomerDto data = createCustomerDto();
        Address address = new Address();
        address.setCity("Columbus");
        address.setState("OH");
        address.setCountry("US");
        address.setLine1("456 Blake St.");
        address.setLine2("");
        address.setPostalCode("45678");

        CustomerDto updateData = new CustomerDto("cus_123",
                                                 address,
                                                 "new_email@example.com",
                                                 "Claudia C. Coulthard",
                                                 "1234567890",
                                                 "pm_789",
                                                 "Updated Test DTO");
        when(customerService.updateCustomer(anyString(), any(CustomerDto.class)))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/customer/cus_123")
                .body(Mono.just(updateData),
                      CustomerDto.class)
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    void shouldHandleDeleteCustomer() {
        when(customerService.deleteCustomer(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .delete()
                .uri("/api/customer/cus_123")
                .exchange()
                .expectStatus().isNoContent();
    }

    // Helper method to create mock StripeCollection
    private StripeCollection<Customer> createMockStripeCollection() {
        StripeCollection<Customer> customers = new StripeCollection<>();

        Customer mockCustomer1 = Mockito.mock(Customer.class);
        when(mockCustomer1.getId()).thenReturn("cus_123");

        Customer mockCustomer2 = Mockito.mock(Customer.class);
        when(mockCustomer2.getId()).thenReturn("cus_456");

        List<Customer> customerList = Arrays.asList(mockCustomer1,
                                                    mockCustomer2);
        customers.setData(customerList);
        return customers;
    }

    private Customer createMockCustomer() {
        Customer customer = Mockito.mock(Customer.class);
        when(customer.getId()).thenReturn("cus_123");
        when(customer.getEmail()).thenReturn("test@example.com");
        when(customer.getName()).thenReturn("Andrew Anderson");
        return customer;
    }

    private CustomerDto createCustomerDto() {
        Address address = new Address();
        address.setCity("Los Angeles");
        address.setState("CA");
        address.setCountry("US");
        address.setLine1("123 Easy St.");
        address.setLine2("APT A");
        address.setPostalCode("12345");
        return new CustomerDto("cus_123",
                               address,
                               "email@example.com",
                               "Claudia Coulthard",
                               "1234567890",
                               "pm_789",
                               "Test DTO");
    }

}

