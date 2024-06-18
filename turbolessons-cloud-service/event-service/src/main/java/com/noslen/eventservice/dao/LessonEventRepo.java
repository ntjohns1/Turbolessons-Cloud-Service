package com.noslen.eventservice.dao;

import com.noslen.eventservice.dto.LessonEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonEventRepo extends JpaRepository<LessonEvent, Integer> {
    List<LessonEvent> findLessonEventByStudent(String student);
    List<LessonEvent> findLessonEventByTeacher(String teacher);
    @Query(value = "SELECT * FROM lesson_event WHERE DATE(date) = :date", nativeQuery = true)
    List<LessonEvent> findLessonEventByDate(@Param("date") LocalDate date);
    @Query(value = "SELECT * FROM lesson_event WHERE teacher = :teacher AND DATE(date) = :date", nativeQuery = true)
    List<LessonEvent> findLessonEventByTeacherAndDate(String teacher, LocalDate date);
}
