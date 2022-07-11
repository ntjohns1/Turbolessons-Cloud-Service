package com.noslen.lsaadminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LsaAdminServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LsaAdminServiceApplication.class, args);
	}

}
