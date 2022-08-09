package com.noslen.eventservice.controller;

import com.noslen.eventservice.dto.LessonEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RefreshScope
public class EventController {


    @GetMapping("/api/event/{day}")
    public Mono<LessonEvent> getLessonEvent(@PathVariable("day") String date, BearerTokenAuthentication auth ) {

        LessonEvent e = new LessonEvent(date);
        return Mono.just(e);
    }
}
