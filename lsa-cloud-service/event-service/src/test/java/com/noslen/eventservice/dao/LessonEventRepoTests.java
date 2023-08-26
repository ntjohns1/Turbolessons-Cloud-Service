package com.noslen.eventservice.dao;

import com.noslen.eventservice.dto.LessonEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.yml",
        properties = { "spring.config.name=application-test" }
)
@Sql(scripts = "/sql/setupH2.sql")
public class LessonEventRepoTests {
//        LocalDateTime date = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 0);
        LocalDateTime date = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 0);
        LocalDateTime otherDate = LocalDateTime.of(2033, Month.AUGUST, 18, 15, 0);

    private final LessonEventRepo lessonRepo;

    @Autowired
    public LessonEventRepoTests(LessonEventRepo lessonRepo) {
        this.lessonRepo = lessonRepo;
    }

    @BeforeEach
    public void setUp() {
        lessonRepo.deleteAll();
    }
    //Get All Lesson Events
    @Test
    public void testFindAllLessons() {
        LessonEvent lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);

        List<LessonEvent> eventList = lessonRepo.findAll();

        Assertions.assertEquals(3, eventList.size());

    }
    //Get One Lesson Event
    @Test
    public void testAddGetDeleteLesson() {
        LessonEvent lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);

        Optional<LessonEvent> lesson1 = lessonRepo.findById(lesson.getId());

        Assertions.assertEquals(lesson1.get(),lesson);
        
        lessonRepo.deleteById(lesson.getId());

        lesson1 = lessonRepo.findById(lesson.getId());

        Assertions.assertFalse(lesson1.isPresent());
    }

    //Get Lesson Events By Teacher
    @Test
    public void testGetLessonEventsByTeacher() {
        LessonEvent lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent1","teststudent@email.com","testTeacher1","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);

        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByTeacher("testTeacher");

        Assertions.assertEquals(2,lessonEventList.size());
        Assertions.assertEquals("testTeacher", lessonEventList.get(0).getTeacher());
        Assertions.assertEquals("testTeacher", lessonEventList.get(1).getTeacher());
    }

    //Get Lesson Events By Student
    @Test
    public void testGetLessonsByStudent() {
        LessonEvent lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent1","teststudent@email.com","testTeacher1","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);

        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByStudent("testStudent");

        Assertions.assertEquals(2,lessonEventList.size());
        Assertions.assertEquals("testStudent", lessonEventList.get(0).getStudent());
        Assertions.assertEquals("testStudent", lessonEventList.get(1).getStudent());
    }

    //Get All Lesson Events by Date


    @Test
    void testGetLessonsByDate() {
        LessonEvent lesson = new LessonEvent("testStudent1","teststudent1@email.com","testTeacher1","testteacher1@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent2","teststudent2@email.com","testTeacher2","testteacher2@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent3","teststudent3@email.com","testTeacher3","testteacher3@email.com",otherDate,"Test");
        lesson = lessonRepo.save(lesson);

        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByDate(date.toLocalDate());

        Assertions.assertEquals(2,lessonEventList.size());
//        Assertions.assertEquals("testTeacher", lessonEventList.get(0).getTeacher());
        System.out.println("expected: " + date);
        System.out.println("actual: " + lessonEventList.get(0).getDate().toLocalDate());
        Assertions.assertEquals(date,lessonEventList.get(0).getDate());
//        Assertions.assertEquals("testTeacher", lessonEventList.get(1).getTeacher());
        Assertions.assertEquals(date,lessonEventList.get(1).getDate());
    }

    //Get Lesson Events By Teacher and Date
    @Test
    public void testGetLessonsByTeacherAndDate() {
        LessonEvent lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent("testStudent1","teststudent@email.com","testTeacher1","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);

        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByTeacherAndDate("testTeacher", date.toLocalDate());

        Assertions.assertEquals(2,lessonEventList.size());
        Assertions.assertEquals("testTeacher", lessonEventList.get(0).getTeacher());
        Assertions.assertEquals(date,lessonEventList.get(0).getDate());
        Assertions.assertEquals("testTeacher", lessonEventList.get(1).getTeacher());
        Assertions.assertEquals(date,lessonEventList.get(1).getDate());
    }

    //Update Lesson Event
    @Test
    public void testUpdateLesson() {
        LocalDateTime date1 = LocalDateTime.of(2034, Month.SEPTEMBER, 18, 16, 30);
        LessonEvent lesson = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date,"Test");
        lesson = lessonRepo.save(lesson);

        lesson.setStudent("testStudentNew");
        lesson.setTeacher("testTeacherNew");
        lesson.setDate(date1);
        lesson.setComments("TestTest");

        lessonRepo.save(lesson);

        Optional<LessonEvent> lesson1 = lessonRepo.findById(lesson.getId());
        Assertions.assertEquals(lesson,lesson1.get());
    }
}
