package com.noslen.eventservice;

import com.noslen.eventservice.dto.LessonEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import java.time.LocalDateTime;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebFluxSecurity
public class EventServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
		LessonEvent lesson = new LessonEvent("6967f66b-8a23-4549-bb5e-0b6612fd4f5a",LocalDateTime.now(),"This is a test LessonEvent");
		System.out.println(lesson.toString());
	}

}
