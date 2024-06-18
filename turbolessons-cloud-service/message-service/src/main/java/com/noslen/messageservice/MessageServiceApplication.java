package com.noslen.messageservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@EnableDiscoveryClient
@SpringBootApplication
public class MessageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessageServiceApplication.class, args);
	}
}
