package com.noslen.lsaconfigservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class LsaConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LsaConfigServiceApplication.class, args);
	}

}
