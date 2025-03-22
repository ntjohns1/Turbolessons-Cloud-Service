package com.turbolessons.paymentservice.util;

import com.turbolessons.paymentservice.dto.LessonEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class EventServiceClientImpl implements EventServiceClient {

    private final WebClient webClient;

    public EventServiceClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Flux<LessonEvent> getEvents(LocalDate date) {
        return webClient.get()
                .uri("http://event-service/api/lessons")
                .retrieve()
                .bodyToFlux(LessonEvent.class);
    }
}
