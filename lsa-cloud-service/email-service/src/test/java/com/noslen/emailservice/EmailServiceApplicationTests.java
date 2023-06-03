package com.noslen.emailservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(properties = "spring.datasource.url=")
@ActiveProfiles("test")
class EmailServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}


