package com.noslen.emailservice.util;

import com.noslen.emailservice.dto.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class EventServiceClient {

    private final WebClient webClient;

    public EventServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Lesson> getEvents(LocalDate date) {
        return webClient
                .get()
                .uri("http://event-service/api/lessons")
                .retrieve()
                .bodyToFlux(Lesson.class);
    }
}

