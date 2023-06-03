package com.noslen.eventservice.controller;


import com.noslen.eventservice.dto.LessonEvent;
import com.noslen.eventservice.service.LessonEventService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//@RefreshScope
@RestController
public class LessonEventController {
    private final LessonEventService service;

    public LessonEventController(LessonEventService service) {
        this.service = service;
    }

    //Get All Lesson Events
    @GetMapping("/api/lessons")
    public List<LessonEvent> getAllLessons() {

        return service.findAllLessonEvents();
    }
    //Get One Lesson Event
    @GetMapping("/api/lessons/{id}")
    public LessonEvent getLessonById(Integer id) {

        return service.findLessonEvent(id);
    }
    //Get Lesson Events By Teacher
    @GetMapping("/api/lessons/teacher/{teacher}")
    public List<LessonEvent> getLessonsByTeacher(String teacher) {

        return service.findLessonEventsByTeacher(teacher);
    }
    //Get Lesson Events By Student
    @GetMapping("/api/lessons/student/{student}")
    public List<LessonEvent> getLessonsByStudent(String student) {

        return service.findLessonEventsByStudent(student);
    }
    //Get Lesson Events By Teacher and Date\
    @GetMapping("/api/lessons/{teacher}/{date}")
    public List<LessonEvent> getLessonsByTeacherAndDate(String teacher, LocalDate date) {

        return service.findLessonEventsByTeacherAndDate(teacher, date);
    }
    //Create Lesson Event
    @PostMapping("/api/lessons")
    public LessonEvent createLesson(LessonEvent lesson) {

        service.saveLessonEvent(lesson);
        return lesson;
    }
    //Update Lesson Event
    @PutMapping("/api/lessons/{id}")
    public void updateLesson(Integer id, LessonEvent lesson) {
        LessonEvent fromService = service.findLessonEvent(id);
        fromService.setTeacher(lesson.getTeacher());
        fromService.setStudent(lesson.getStudent());
        fromService.setDate(lesson.getDate());
        fromService.setComments(lesson.getComments());
        service.saveLessonEvent(fromService);
    }
    //Delete Lesson Event

    @DeleteMapping("/api/lessons/{id}")
    public void deleteLesson(Integer id) {
        service.deleteLessonEvent(id);
    }
}
