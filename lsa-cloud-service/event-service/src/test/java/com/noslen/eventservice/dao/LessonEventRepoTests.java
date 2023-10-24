package com.noslen.eventservice.dao;

import com.noslen.eventservice.dto.LessonEvent;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
@Sql(scripts = "/sql/setupH2.sql")
public class LessonEventRepoTests {
    //        LocalDateTime date = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 0);
    LocalDateTime start1 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            15,
                                            0);
    LocalDateTime end1 = start1.plusHours(1L);
    LocalDateTime start2 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            16,
                                            0);
    LocalDateTime end2 = start2.plusHours(1L);
    LocalDateTime start3 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            18,
                                            15,
                                            0);
    LocalDateTime end3 = start3.plusHours(1L);
    String title1 = "StudentA - 3:00PM";
    String title2 = "StudentB - 3:00PM"; //8/18
    String title3 = "StudentC - 4:00PM";
    String student1 = "StudentA";
    String student2 = "StudentB";
    String student3 = "StudentC";
    String teacher1 = "TeacherA";
    String teacher2 = "TeacherB";
    String studentEmail1 = "student_a@example.com";
    String studentEmail2 = "student_b@example.com";
    String studentEmail3 = "student_c@example.com";
    String teacherEmail1 = "teacher_a@example.com";
    String teacherEmail2 = "teacher_b@example.com";
    String comments = "test test";

    LessonEvent lesson;


    private final LessonEventRepo lessonRepo;

    @Autowired
    public LessonEventRepoTests(LessonEventRepo lessonRepo) {
        this.lessonRepo = lessonRepo;
    }

    @BeforeEach
    public void setUp() {
        lessonRepo.deleteAll();
        lesson = new LessonEvent(start1,
                                 end1,
                                 title1,
                                 student1,
                                 studentEmail1,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent(start2,
                                 end2,
                                 title2,
                                 student2,
                                 studentEmail2,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        lesson = lessonRepo.save(lesson);
        lesson = new LessonEvent(start3,
                                 end3,
                                 title3,
                                 student3,
                                 studentEmail3,
                                 teacher2,
                                 teacherEmail2,
                                 comments);
        lesson = lessonRepo.save(lesson);
    }

    //Get All Lesson Events
    @Test
    public void testFindAllLessons() {


        List<LessonEvent> eventList = lessonRepo.findAll();

        Assertions.assertEquals(3,
                                eventList.size());

    }

    //Get One Lesson Event
    @Test
    public void testAddGetDeleteLesson() {

        Optional<LessonEvent> lesson1 = lessonRepo.findById(lesson.getId());

        Assertions.assertEquals(lesson1.get(),
                                lesson);

        lessonRepo.deleteById(lesson.getId());

        lesson1 = lessonRepo.findById(lesson.getId());

        Assertions.assertFalse(lesson1.isPresent());
    }

    //Get Lesson Events By Teacher
    @Test
    public void testGetLessonEventsByTeacher() {
        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByTeacher(teacher1);
        String expected = "TeacherA";
        Assertions.assertEquals(2,
                                lessonEventList.size());
        Assertions.assertEquals(expected,
                                lessonEventList.get(0)
                                        .getTeacher());
        Assertions.assertEquals(expected,
                                lessonEventList.get(1)
                                        .getTeacher());
    }

    //Get Lesson Events By Student
    @Test
    public void testGetLessonsByStudent() {

        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByStudent(student1);
        String expected = "StudentA";

        Assertions.assertEquals(1,
                                lessonEventList.size());
        Assertions.assertEquals(expected,
                                lessonEventList.get(0)
                                        .getStudent());
    }

    //Get All Lesson Events by Date


    @Test
    void testGetLessonsByDate() {
        LocalDate expected = start1.toLocalDate();
        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByDate(start1.toLocalDate());

        Assertions.assertEquals(2,
                                lessonEventList.size());
//        Assertions.assertEquals("testTeacher", lessonEventList.get(0).getTeacher());
        System.out.println("expected: " + start1.toLocalDate());
        System.out.println("actual: " + lessonEventList.get(0)
                .getDate());
        Assertions.assertEquals(expected,
                                lessonEventList.get(0)
                                        .getDate());
//        Assertions.assertEquals("testTeacher", lessonEventList.get(1).getTeacher());
        Assertions.assertEquals(expected,
                                lessonEventList.get(1)
                                        .getDate());
    }

    //Get Lesson Events By Teacher and Date
    @Test
    public void testGetLessonsByTeacherAndDate() {

        LocalDate expectedDate = start1.toLocalDate();
        String expectedTeacher = "TeacherA";
        List<LessonEvent> lessonEventList = lessonRepo.findLessonEventByTeacherAndDate(teacher1, start1.toLocalDate());

        Assertions.assertEquals(2,
                                lessonEventList.size());
        Assertions.assertEquals(expectedTeacher,
                                lessonEventList.get(0)
                                        .getTeacher());
        Assertions.assertEquals(expectedDate,
                                lessonEventList.get(0)
                                        .getDate());
        Assertions.assertEquals(expectedTeacher,
                                lessonEventList.get(1)
                                        .getTeacher());
        Assertions.assertEquals(expectedDate,
                                lessonEventList.get(1)
                                        .getDate());
    }

    //Update Lesson Event
    @Test
    public void testUpdateLesson() {
        LocalDateTime update = LocalDateTime.of(2034,
                                               Month.SEPTEMBER,
                                               18,
                                               16,
                                               30);

        lesson.setStudent("updatedStudent");
        lesson.setStudentEmail("updated_student@example.com");
        lesson.setTeacher("updatedTeacher");
        lesson.setTeacherEmail("updated_teacher@example.com");
        lesson.setStartTime(update);
        lesson.setEndTime(update.plusHours(1L));
        lesson.setComments("UpdatedText");

        lessonRepo.save(lesson);

        Optional<LessonEvent> lesson1 = lessonRepo.findById(lesson.getId());
        Assertions.assertEquals(lesson,
                                lesson1.get());
    }
}
