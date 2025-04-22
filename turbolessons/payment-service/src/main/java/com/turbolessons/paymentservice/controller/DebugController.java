package com.turbolessons.paymentservice.controller;

import com.turbolessons.paymentservice.dto.LessonEvent;
import com.turbolessons.paymentservice.service.meter.LessonMeterEventService;
import com.turbolessons.paymentservice.util.EventServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments/debug")
@Profile("!test") // Don't load this controller in test profile
public class DebugController {

    private static final Logger log = LoggerFactory.getLogger(DebugController.class);
    
    private final EventServiceClient eventServiceClient;
    private final LessonMeterEventService lessonMeterEventService;
    
    @Autowired
    public DebugController(EventServiceClient eventServiceClient, LessonMeterEventService lessonMeterEventService) {
        this.eventServiceClient = eventServiceClient;
        this.lessonMeterEventService = lessonMeterEventService;
    }
    
    @PostConstruct
    public void init() {
        log.info("DebugController initialized with endpoints: /api/payments/debug/events, /api/payments/debug/process-lessons and /api/payments/debug/ping");
    }
    
    @GetMapping("/ping")
    public Mono<Map<String, Object>> ping() {
        log.info("Debug endpoint: Ping received");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Payment service debug endpoint is working");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }
    
    @GetMapping("/events")
    public Flux<LessonEvent> getEvents(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        log.info("Debug endpoint: Fetching events for date {}", targetDate);
        
        return eventServiceClient.getEvents(targetDate)
            .doOnNext(event -> log.info("Received event: {}", event))
            .doOnError(error -> log.error("Error fetching events", error))
            .doOnComplete(() -> log.info("Completed fetching events"));
    }
    
    @GetMapping("/process-lessons")
    public ResponseEntity<String> processLessons() {
        log.info("Debug endpoint: Manually triggering processCompletedLessons");
        try {
            lessonMeterEventService.processCompletedLessons();
            return ResponseEntity.ok("Process completed lessons triggered successfully");
        } catch (Exception e) {
            log.error("Error triggering processCompletedLessons", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
