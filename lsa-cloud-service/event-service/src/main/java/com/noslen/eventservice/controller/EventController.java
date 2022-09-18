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


    @GetMapping("/api/lesson/token")
    public String getToken(BearerTokenAuthentication auth ) {
        String path = System.getenv("PATH");
        System.out.println(path);
        Map<String, Object> m = auth.getTokenAttributes();
        boolean s = m.get("realm_access").toString().contains("admin");
        return s ? "yes" : "no";
    }

    @Autowired
    private LessonEventRepo lessonEventRepo;

    //    GET ALL LESSONS
    @GetMapping("/api/lesson/")
    @ResponseStatus(HttpStatus.OK)
    public List<LessonEvent> getAllLessons() {
        return lessonEventRepo.findAll();
    }

    //GET LESSON BY ID
    @GetMapping("/api/lesson/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LessonEvent> getLessonById(@PathVariable Integer id) {
        Optional<LessonEvent> lesson = lessonEventRepo.findById(id);
        return Mono.justOrEmpty(lesson);
    }



    //GET LESSONS BY STUDENT ID
    @GetMapping("/api/lesson/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<LessonEvent> getLessonsByStudent(@PathVariable String studentId) {
        return lessonEventRepo.findLessonsByStudentId(studentId);
    }

    //CREATE NEW LESSON
    @PostMapping("/api/lesson/")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<LessonEvent> createLesson(@RequestBody  LessonEvent lesson, BearerTokenAuthentication auth ) {
        lessonEventRepo.save(lesson);
        return Mono.just(lesson);
    }

    //UPDATE LESSON BY ID
    @PutMapping("/api/lesson/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateLesson(@RequestBody LessonEvent lesson, @PathVariable Integer id) {
        if(lesson.getId() != id) {
            throw new IllegalArgumentException("Entered ID does not match existing lesson ID");
        }
        lessonEventRepo.save(lesson);
    }

    //DELETE LESSON BY ID
    @DeleteMapping("/api/lesson/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLesson(@PathVariable Integer id) {
        lessonEventRepo.deleteById(id);
    }
}
