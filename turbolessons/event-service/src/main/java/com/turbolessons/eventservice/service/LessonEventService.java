package com.turbolessons.eventservice.service;

import com.turbolessons.eventservice.dao.LessonEventRepo;
import com.turbolessons.eventservice.dto.LessonEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LessonEventService {

    private final LessonEventRepo repository;

    public LessonEventService(LessonEventRepo lessonEventRepo) {
        this.repository = lessonEventRepo;
    }

    //Get All Lesson Event Events

    public List<LessonEvent> findAllLessonEvents() {
        return this.repository.findAll();
    }
    //Get One Lesson Event
    public LessonEvent findLessonEvent(Integer id) {
        Optional<LessonEvent> lesson = this.repository.findById(id);
        return lesson.orElse(null);
    }

    //Get Lesson Event Events By Teacher
    public List<LessonEvent> findLessonEventsByTeacher(String teacher) {
        return repository.findLessonEventByTeacher(teacher);
    }

    //Get Lesson Event Events By Student
    public List<LessonEvent> findLessonEventsByStudent(String student) {
        return repository.findLessonEventByStudent(student);
    }

    public List<LessonEvent> findLessonEventsByDate(LocalDate date) {
        return repository.findLessonEventByDate(date);
    }

    //Get Lesson Event Events By Teacher and Date
    public List<LessonEvent> findLessonEventsByTeacherAndDate(String teacher, LocalDate date) {
        return repository.findLessonEventByTeacherAndDate(teacher,date);
    }

    //Create Lesson Event
    public LessonEvent saveLessonEvent(LessonEvent lesson) {
        return this.repository.save(lesson);
    }

    //Update Lesson Event
    public void updateLessonEvent(Integer id, LessonEvent lesson) {
        Optional<LessonEvent> fromRepo = repository.findById(id);
        fromRepo.ifPresent(existingLessonEvent -> {
            existingLessonEvent.setStartTime(lesson.getStartTime());
            existingLessonEvent.setEndTime(lesson.getEndTime());
            existingLessonEvent.setTitle(lesson.getTitle());
            existingLessonEvent.setStudent(lesson.getStudent());
            existingLessonEvent.setStudentEmail(lesson.getStudentEmail());
            existingLessonEvent.setTeacher(lesson.getTeacher());
            existingLessonEvent.setTeacherEmail(lesson.getTeacherEmail());
            existingLessonEvent.setComments(lesson.getComments());
            repository.save(existingLessonEvent);
        });
    }


    //Delete Lesson Event
    public void deleteLessonEvent(Integer id) {
        this.repository.deleteById(id);
    }

}
