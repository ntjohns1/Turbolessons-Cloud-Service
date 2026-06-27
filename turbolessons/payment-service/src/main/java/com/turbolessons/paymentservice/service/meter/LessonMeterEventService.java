package com.turbolessons.paymentservice.service.meter;

import com.turbolessons.paymentservice.dto.BillingStatus;
import com.turbolessons.paymentservice.dto.LessonEvent;
import com.turbolessons.paymentservice.dto.MeterEventData;
import com.turbolessons.paymentservice.service.customer.CustomerService;
import com.turbolessons.paymentservice.util.EventServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class LessonMeterEventService {

    // Must match the Stripe "lessons" meter's event name (SUM aggregation of value).
    private static final String LESSON_METER_EVENT_NAME = "lessons";

    private final EventServiceClient eventServiceClient;
    private final MeterService meterService;
    private final CustomerService customerService;

    public LessonMeterEventService(EventServiceClient eventServiceClient,
                                   MeterService meterService,
                                   CustomerService customerService) {
        this.eventServiceClient = eventServiceClient;
        this.meterService = meterService;
        this.customerService = customerService;
    }

    @Scheduled(cron ="0 0 23 * * *")
    public void processCompletedLessons() {
        log.info("Starting lesson meter event processing");
        LocalDate today = LocalDate.now();
        
        eventServiceClient.getEvents(today)
            .doOnSubscribe(s -> log.info("Subscribing to event stream for date: {}", today))
            .doOnError(error -> log.error("Error in event stream subscription: {}", error.getMessage(), error))
            .filter(this::isUnloggedCompletedLesson)
            .doOnNext(lesson -> log.info("Found unlogged completed lesson: {}", lesson.getId()))
            .flatMap(this::createMeterEventForLesson)
            .subscribe(
                success -> log.info("Successfully processed lesson meter event"),
                error -> log.error("Error processing lesson meter event", error),
                () -> log.info("Completed processing all lesson meter events")
            );
    }

    private boolean isUnloggedCompletedLesson(LessonEvent lesson) {
        return lesson.getBillingStatus() == BillingStatus.UNLOGGED &&
               lesson.getEndTime() != null &&
               lesson.getEndTime().isBefore(LocalDate.now().atStartOfDay());
    }

    private Mono<MeterEventData> createMeterEventForLesson(LessonEvent lesson) {
        String identifier = UUID.randomUUID().toString();
        String value = meterValueForLesson(lesson);

        // Resolve the real Stripe customer id from the student's email — the meter
        // aggregates per stripe_customer_id, so email is not a valid payload value.
        return customerService.searchCustomerByEmail(lesson.getStudentEmail())
            .switchIfEmpty(Mono.error(new IllegalStateException(
                "No Stripe customer found for student email: " + lesson.getStudentEmail())))
            .flatMap(customer -> {
                MeterEventData meterEventData = new MeterEventData(
                    identifier,
                    LESSON_METER_EVENT_NAME,
                    customer.getId(),
                    value
                );
                return meterService.createMeterEvent(meterEventData)
                    .doOnSuccess(event -> updateLessonBillingStatus(lesson));
            })
            .doOnError(error -> log.error("Failed to create meter event for lesson {}", lesson.getId(), error));
    }

    // 30-minute lesson = 1 unit, 1-hour (or longer) = 2 units, against the
    // $30/30-min metered price.
    private String meterValueForLesson(LessonEvent lesson) {
        if (lesson.getStartTime() == null || lesson.getEndTime() == null) {
            return "1";
        }
        long minutes = Duration.between(lesson.getStartTime(), lesson.getEndTime()).toMinutes();
        return minutes >= 60 ? "2" : "1";
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
