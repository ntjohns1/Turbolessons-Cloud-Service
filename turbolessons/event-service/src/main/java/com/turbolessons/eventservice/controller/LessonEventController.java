package com.turbolessons.eventservice.controller;


import com.turbolessons.eventservice.dto.LessonEvent;
import com.turbolessons.eventservice.service.LessonEventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SCOPE_email_client')")
    @GetMapping("/api/lessons")
    public List<LessonEvent> getAllLessons() {

        return service.findAllLessonEvents();
    }
    //Get One Lesson Event
    @GetMapping("/api/lessons/{id}")
    public LessonEvent getLessonById(@PathVariable Integer id) {

        return service.findLessonEvent(id);
    }
    //Get Lesson Events By Teacher
    @GetMapping("/api/lessons/teacher/{teacher}")
    public List<LessonEvent> getLessonsByTeacher(@PathVariable String teacher) {

        return service.findLessonEventsByTeacher(teacher);
    }
    //Get Lesson Events By Student
    @GetMapping("/api/lessons/student/{student}")
    public List<LessonEvent> getLessonsByStudent(@PathVariable String student) {

        return service.findLessonEventsByStudent(student);
    }
    //Get Lesson Events By Date
    @PreAuthorize("hasAuthority('SCOPE_email_client')")
    @GetMapping("/api/lessons/date/{date}")
    public List<LessonEvent> getLessonsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findLessonEventsByDate(date);
    }

    //Get Lesson Events By Teacher and Date
    @GetMapping("/api/lessons/{teacher}/{date}")
    public List<LessonEvent> getLessonsByTeacherAndDate(@PathVariable String teacher, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findLessonEventsByTeacherAndDate(teacher, date);
    }
    //Create Lesson Event
    @PostMapping("/api/lessons")
    public LessonEvent createLesson(@RequestBody LessonEvent lesson) {

        service.saveLessonEvent(lesson);
        return lesson;
    }
    //Update Lesson Event
    @PutMapping("/api/lessons/{id}")
    public void updateLesson(@PathVariable Integer id, @RequestBody LessonEvent lesson) {
        LessonEvent fromService = service.findLessonEvent(id);
        fromService.setStartTime(lesson.getStartTime());
        fromService.setEndTime(lesson.getEndTime());
        fromService.setTitle(lesson.getTitle());
        fromService.setStudent(lesson.getStudent());
        fromService.setStudentEmail(lesson.getStudentEmail());
        fromService.setTeacher(lesson.getTeacher());
        fromService.setTeacherEmail(lesson.getTeacherEmail());
        fromService.setComments(lesson.getComments());
        fromService.setBillingStatus(lesson.getBillingStatus());
        service.saveLessonEvent(fromService);
    }
    //Delete Lesson Event

    @DeleteMapping("/api/lessons/{id}")
    public void deleteLesson(@PathVariable Integer id) {
        service.deleteLessonEvent(id);
    }
}

