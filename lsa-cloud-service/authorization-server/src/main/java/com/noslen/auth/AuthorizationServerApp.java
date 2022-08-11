package com.noslen.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;




@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
@EnableEurekaServer
//@ConfigurationProperties(prefix = "spring.liquibase", ignoreUnknownFields = false)
public class AuthorizationServerApp {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServerApp.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AuthorizationServerApp.class, args);
	}


}
