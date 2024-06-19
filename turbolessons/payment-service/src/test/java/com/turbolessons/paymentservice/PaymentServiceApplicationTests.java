package com.turbolessons.paymentservice;

import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.turbolessons.paymentservice.service.StripeOperation;
import com.turbolessons.paymentservice.service.StripeVoidOperation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

@SpringBootTest(properties = "spring.config.name=application-test")
@ActiveProfiles("test")
class PaymentServiceApplicationTests {

	@Bean
	public StripeClientHelper stripeClientHelper() {
		return new StripeClientHelper() {
			@Override
			public <T> Mono<T> executeStripeCall(StripeOperation<T> stripeOperation) {
				return null;
			}

			@Override
			public Mono<Void> executeStripeVoidCall(StripeVoidOperation stripeOperation) {
				return null;
			}
		};
	}

	@MockBean
	private StripeClientHelper stripeClientHelper;

	@Test
	void contextLoads() {
	}

}
