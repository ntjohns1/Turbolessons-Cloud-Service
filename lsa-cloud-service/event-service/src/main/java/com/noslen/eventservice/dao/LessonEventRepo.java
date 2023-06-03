package com.noslen.eventservice.dao;

import com.noslen.eventservice.dto.LessonEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonEventRepo extends JpaRepository<LessonEvent, Integer> {
    List<LessonEvent> findLessonEventByStudent(String student);
    List<LessonEvent> findLessonEventByTeacher(String teacher);
    List<LessonEvent> findLessonEventByDate(LocalDate date);
    List<LessonEvent> findLessonEventByTeacherAndDate(String teacher, LocalDate date);
}
