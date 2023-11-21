package com.noslen.paymentservice.controller.paymentmethod;

import com.noslen.paymentservice.controller.customer.CustomerHandlerImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@Log4j2
@WebFluxTest
@Import(PaymentMethodHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class PaymentMethodHandlerTests {


}
