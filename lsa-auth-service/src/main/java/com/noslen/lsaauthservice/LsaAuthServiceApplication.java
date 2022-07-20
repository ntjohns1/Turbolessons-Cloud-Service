package com.noslen.lsaauthservice;

import com.noslen.lsaauthservice.repository.MapCustomUserRepository;
import com.noslen.lsaauthservice.model.CustomUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LsaAuthServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LsaAuthServiceApplication.class, args);
	}

}
