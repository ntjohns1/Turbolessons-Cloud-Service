package com.noslen.emailservice.util;

import com.noslen.emailservice.dto.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class EventServiceClient {

    @Autowired
    private WebClient webClient;

    public Flux<Lesson> getEvents(LocalDate date) {
        return webClient.get()
                .uri("lb://event-service/api/lessons/date/" + date)
                .retrieve()
                .bodyToFlux(Lesson.class);
    }
}
