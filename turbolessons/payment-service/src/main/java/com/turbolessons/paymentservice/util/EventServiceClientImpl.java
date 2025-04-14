package com.turbolessons.paymentservice.util;

import com.turbolessons.paymentservice.dto.LessonEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EventServiceClientImpl implements EventServiceClient {

    private final WebClient webClient;
    private static final String EVENT_SERVICE_BASE_URL = "http://event-service/api/lessons";

    public EventServiceClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Flux<LessonEvent> getEvents(LocalDate date) {
        return webClient.get()
                .uri(EVENT_SERVICE_BASE_URL + "/date/{date}", date.format(DateTimeFormatter.ISO_DATE))
                .retrieve()
                .bodyToFlux(LessonEvent.class);
    }

    public Mono<LessonEvent> updateEvent(Integer id, LessonEvent event) {
        return webClient.put()
                .uri(EVENT_SERVICE_BASE_URL + "/{id}", id)
                .bodyValue(event)
                .retrieve()
                .bodyToMono(LessonEvent.class);
    }
}
