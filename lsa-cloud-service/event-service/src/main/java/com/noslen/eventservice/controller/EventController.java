package com.noslen.eventservice.controller;

import com.noslen.eventservice.dta.LessonEventRepo;
import com.noslen.eventservice.dto.LessonEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RefreshScope
public class EventController {


    @GetMapping("/api/event/token")
    public String getToken(BearerTokenAuthentication auth ) {
        Map<String, Object> m = auth.getTokenAttributes();
        m.forEach((key, value) -> System.out.println(key + ": " + value.toString()));
        return auth.getToken().getTokenValue();
    }

    @Autowired
    private LessonEventRepo lessonEventRepo;

    //    GET ALL LESSONS
    @GetMapping("/api/event/")
    @ResponseStatus(HttpStatus.OK)
    public List<LessonEvent> getAllLessons() {
        return lessonEventRepo.findAll();
    }

    //GET LESSON BY ID
    @GetMapping("/api/event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LessonEvent> getLessonById(@PathVariable Integer id) {
        Optional<LessonEvent> lesson = lessonEventRepo.findById(id);
        return Mono.justOrEmpty(lesson);
    }



    //GET LESSONS BY STUDENT ID
    @GetMapping("/api/event/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<LessonEvent> getLessonsByStudent(@PathVariable String studentId) {
        return lessonEventRepo.findLessonsByStudentId(studentId);
    }

    //CREATE NEW LESSON
    @PostMapping("/api/event/")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonEvent createLesson(@RequestBody  LessonEvent lesson) {
        lessonEventRepo.save(lesson);
        return lesson;
    }

    //UPDATE LESSON BY ID
    @PutMapping("/api/event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateLesson(@RequestBody LessonEvent lesson, @PathVariable Integer id) {
        if(lesson.getId() != id) {
            throw new IllegalArgumentException("Entered ID does not match existing lesson ID");
        }
        lessonEventRepo.save(lesson);
    }

    //DELETE LESSON BY ID
    @DeleteMapping("/api/event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLesson(@PathVariable Integer id) {
        lessonEventRepo.deleteById(id);
    }
}
