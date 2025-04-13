package com.turbolessons.paymentservice.service.meter;

import com.turbolessons.paymentservice.dto.BillingStatus;
import com.turbolessons.paymentservice.dto.LessonEvent;
import com.turbolessons.paymentservice.dto.MeterEventData;
import com.turbolessons.paymentservice.util.EventServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class LessonMeterEventService {

    private final EventServiceClient eventServiceClient;
    private final MeterService meterService;

    public LessonMeterEventService(EventServiceClient eventServiceClient, MeterService meterService) {
        this.eventServiceClient = eventServiceClient;
        this.meterService = meterService;
    }

    @Scheduled(cron = "0 0 23 * * *") // Run at 11 PM every day
    public void processCompletedLessons() {
        log.info("Starting daily lesson meter event processing");
        LocalDate today = LocalDate.now();
        
        eventServiceClient.getEvents(today)
            .filter(this::isUnloggedCompletedLesson)
            .flatMap(this::createMeterEventForLesson)
            .subscribe(
                success -> log.info("Successfully processed lesson meter event"),
                error -> log.error("Error processing lesson meter event", error)
            );
    }

    private boolean isUnloggedCompletedLesson(LessonEvent lesson) {
        return lesson.getBillingStatus() == BillingStatus.UNLOGGED &&
               lesson.getEndTime() != null &&
               lesson.getEndTime().isBefore(LocalDate.now().atStartOfDay());
    }

    private Mono<MeterEventData> createMeterEventForLesson(LessonEvent lesson) {
        String identifier = UUID.randomUUID().toString();
        
        MeterEventData meterEventData = new MeterEventData(
            identifier,
            "lesson.completed",
            lesson.getStudentEmail(), // Assuming student email is their Stripe customer ID
            "1" // Each lesson counts as 1 unit
        );

        return meterService.createMeterEvent(meterEventData)
            .doOnSuccess(event -> updateLessonBillingStatus(lesson))
            .doOnError(error -> log.error("Failed to create meter event for lesson {}", lesson.getId(), error));
    }

    private void updateLessonBillingStatus(LessonEvent lesson) {
        lesson.setBillingStatus(BillingStatus.LOGGED);
        eventServiceClient.updateEvent(lesson.getId(), lesson)
            .subscribe(
                updated -> log.info("Lesson {} billing status updated to LOGGED", lesson.getId()),
                error -> log.error("Failed to update lesson {} billing status", lesson.getId(), error)
            );
    }
}
