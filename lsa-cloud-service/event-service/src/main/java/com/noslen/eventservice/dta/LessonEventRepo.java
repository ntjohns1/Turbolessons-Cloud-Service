package com.noslen.eventservice.dta;

import com.noslen.eventservice.dto.LessonEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonEventRepo extends JpaRepository<LessonEvent, Integer> {
    List<LessonEvent> findLessonsByStudentId(String studentId);
}
