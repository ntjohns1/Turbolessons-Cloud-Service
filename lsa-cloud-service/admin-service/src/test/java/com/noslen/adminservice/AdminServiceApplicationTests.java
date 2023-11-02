package com.noslen.adminservice;

import org.junit.jupiter.api.Test;
import org.openapitools.client.api.GroupApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.config.name=application-test")
@ActiveProfiles("test")
class AdminServiceApplicationTests {

	@MockBean
	private GroupApi groupApi;
	@Test
	void contextLoads() {
	}

}
