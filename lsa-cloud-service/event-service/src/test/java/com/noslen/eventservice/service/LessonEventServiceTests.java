package com.noslen.eventservice.service;

import com.noslen.eventservice.dao.LessonEventRepo;
import com.noslen.eventservice.dto.LessonEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(properties = "spring.config.name=application-test")
@ActiveProfiles("test")
public class LessonEventServiceTests {

    private LessonEventRepo lessonEventRepo;
    private LessonEventService service;
    LocalDateTime date1 = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 0);
    LocalDateTime date2 = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 30);
    LocalDateTime date3 = LocalDateTime.of(2033, Month.AUGUST, 17, 16, 0);
    LocalDateTime date4 = LocalDateTime.of(2033, Month.AUGUST, 18, 15, 30);
    Map<Integer, LessonEvent> lessonEventStore;
    Map<String, List<LessonEvent>> lessonEventByStudentStore;
    Map<String, List<LessonEvent>> lessonEventByTeacherStore;
    Map<String, List<LessonEvent>> lessonEventByDateStore;
    Map<String, List<LessonEvent>> lessonEventByTeacherAndDateStore;

    @BeforeEach
    public void setUp() {
        lessonEventStore = new HashMap<>();
        lessonEventByStudentStore = new HashMap<>();
        lessonEventByTeacherStore = new HashMap<>();
        lessonEventByDateStore = new HashMap<>();
        lessonEventByTeacherAndDateStore = new HashMap<>();
        setUpLessonEventRepoMock();
        service = new LessonEventService(lessonEventRepo);
    }

    //Get All Lesson Event Events
    @Test
    public void testFindAllLessonEvents() {

        LessonEvent lesson1 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date1, "Test");
        LessonEvent lesson2 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date2, "Test");
        LessonEvent lesson3 = new LessonEvent("testStudent1", "teststudent@email.com", "testTeacher1", "testteacher@email.com", date3, "TestTest");
        LessonEvent lesson4 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date4, "Test");

        service.saveLessonEvent(lesson1);
        service.saveLessonEvent(lesson2);
        service.saveLessonEvent(lesson3);
        service.saveLessonEvent(lesson4);
        System.out.println("****** Map Values: \n" + lessonEventStore.values());

        List<LessonEvent> allLessons = service.findAllLessonEvents();
        Assertions.assertEquals(4, allLessons.size());
        Assertions.assertEquals("testTeacher", allLessons.get(0).getTeacher());
        Assertions.assertEquals("testStudent", allLessons.get(0).getStudent());
        Assertions.assertEquals(date1, allLessons.get(0).getDate());
        Assertions.assertEquals("Test", allLessons.get(0).getComments());
        Assertions.assertEquals("testTeacher1", allLessons.get(2).getTeacher());
        Assertions.assertEquals("testStudent1", allLessons.get(2).getStudent());
        Assertions.assertEquals(date3, allLessons.get(2).getDate());
        Assertions.assertEquals("TestTest", allLessons.get(2).getComments());
        lessonEventStore.clear();
    }

    //Get One Lesson Event
    @Test
    public void shouldSaveFindDeleteLessonEvent() {
        LessonEvent lesson = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date1, "Test");
        lesson = service.saveLessonEvent(lesson);
        LessonEvent fromService = service.findLessonEvent(lesson.getId());
        Assertions.assertEquals(lesson, fromService);
        service.deleteLessonEvent(lesson.getId());
        fromService = service.findLessonEvent(lesson.getId());
        Assertions.assertNull(fromService);
        lessonEventStore.clear();
    }

    //Get Lesson Event Events By Teacher
    @Test
    public void shouldFindLessonEventByTeacher() {
        // Create and save 3 lessons with the same teacher and date
        LessonEvent lesson1 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date1, "Test");
        LessonEvent lesson2 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date2, "Test");
        LessonEvent lesson3 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date3, "Test");

        service.saveLessonEvent(lesson1);
        service.saveLessonEvent(lesson2);
        service.saveLessonEvent(lesson3);

        List<LessonEvent> fromService = service.findLessonEventsByTeacher("testTeacher");
        Assertions.assertEquals(3, fromService.size());
        Assertions.assertEquals("testTeacher", fromService.get(0).getTeacher());
        lessonEventByTeacherStore.clear();
    }

    //Get Lesson Event Events By Student
    @Test
    public void shouldFindLessonEventByStudent() {
        // Create and save 3 lessons with the same teacher and date
        LessonEvent lesson1 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date1, "Test");
        LessonEvent lesson2 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date2, "Test");
        LessonEvent lesson3 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date3, "Test");

        service.saveLessonEvent(lesson1);
        service.saveLessonEvent(lesson2);
        service.saveLessonEvent(lesson3);

        List<LessonEvent> fromService = service.findLessonEventsByStudent("testStudent");
        Assertions.assertEquals(3, fromService.size());
        Assertions.assertEquals("testStudent", fromService.get(0).getStudent());
        lessonEventByStudentStore.clear();
    }

    //Get Lesson Event Events By Teacher and Date
    @Test
    public void shouldFindLessonEventByDate() {
        // Create and save 3 lessons with the same teacher and date
        LessonEvent lesson1 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date1, "Test");
        LessonEvent lesson2 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date2, "Test");
        LessonEvent lesson3 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date3, "Test");
        service.saveLessonEvent(lesson1);
        service.saveLessonEvent(lesson2);
        service.saveLessonEvent(lesson3);

        // Call the method we want to test
        List<LessonEvent> fromService = service.findLessonEventsByDate(date1.toLocalDate());

        // Assert that the result is as expected
        Assertions.assertEquals(3, fromService.size());
        Assertions.assertEquals("testTeacher", fromService.get(0).getTeacher());
        Assertions.assertEquals(date1, fromService.get(0).getDate());
        lessonEventByDateStore.clear();
    }


    @Test
    public void shouldFindLessonEventByTeacherAndDate() {
        // Create and save 3 lessons with the same teacher and date
        LessonEvent lesson1 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date1, "Test");
        LessonEvent lesson2 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date2, "Test");
        LessonEvent lesson3 = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date3, "Test");
        service.saveLessonEvent(lesson1);
        service.saveLessonEvent(lesson2);
        service.saveLessonEvent(lesson3);

        // Call the method we want to test
        List<LessonEvent> fromService = service.findLessonEventsByTeacherAndDate("testTeacher", date1.toLocalDate());

        // Assert that the result is as expected
        Assertions.assertEquals(3, fromService.size());
        Assertions.assertEquals("testTeacher", fromService.get(0).getTeacher());
        Assertions.assertEquals(date1, fromService.get(0).getDate());
        lessonEventByTeacherAndDateStore.clear();
    }

    //Update Lesson Event
    @Test
    public void shouldUpdateLessonEvent() {
        LessonEvent lesson = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date1, "Test");
        LessonEvent updatedLesson = new LessonEvent("testStudent", "teststudent@email.com", "testTeacher", "testteacher@email.com", date2, "Test");
        lesson = service.saveLessonEvent(lesson);
        service.updateLessonEvent(lesson.getId(), updatedLesson);
        LessonEvent updatedFromService = service.findLessonEvent(lesson.getId());
        Assertions.assertEquals(updatedLesson.getStudent(), updatedFromService.getStudent());
        Assertions.assertEquals(updatedLesson.getTeacher(), updatedFromService.getTeacher());
        Assertions.assertEquals(date2, updatedFromService.getDate());
        Assertions.assertEquals(updatedLesson.getComments(), updatedFromService.getComments());
        lessonEventStore.clear();
    }

    private void setUpLessonEventRepoMock() {

        lessonEventRepo = mock(LessonEventRepo.class);

        Mockito.when(lessonEventRepo.findAll()).thenAnswer(invocation -> new ArrayList<>(lessonEventStore.values()));

        Mockito.when(lessonEventRepo.save(any(LessonEvent.class))).then(invocation -> {
            LessonEvent lesson = invocation.getArgument(0);
            int newId = lessonEventStore.size() + 1;
            lesson.setId(newId);
            lessonEventStore.put(newId, lesson);
            lessonEventByStudentStore.computeIfAbsent(lesson.getStudent(), k -> new ArrayList<>()).add(lesson);
            lessonEventByTeacherStore.computeIfAbsent(lesson.getTeacher(), k -> new ArrayList<>()).add(lesson);
            String teacherAndDateKey = lesson.getTeacher() + "_" + lesson.getDate().toLocalDate().toString();
            lessonEventByTeacherAndDateStore.computeIfAbsent(teacherAndDateKey, k -> new ArrayList<>()).add(lesson);
            lessonEventByDateStore.computeIfAbsent(lesson.getDate().toLocalDate().toString(), k -> new ArrayList<>()).add(lesson);
            return lesson;
        });

        Mockito.when(lessonEventRepo.findById(any(Integer.class))).then(invocation -> {
            Integer id = invocation.getArgument(0);
            return Optional.ofNullable(lessonEventStore.get(id));
        });

        Mockito.when(lessonEventRepo.findLessonEventByStudent(any(String.class))).then(invocation -> {
            String student = invocation.getArgument(0);
            return Optional.ofNullable(lessonEventByStudentStore.get(student)).orElse(new ArrayList<>());
        });

        Mockito.when(lessonEventRepo.findLessonEventByTeacher(any(String.class))).then(invocation -> {
            String teacher = invocation.getArgument(0);
            return Optional.ofNullable(lessonEventByTeacherStore.get(teacher)).orElse(new ArrayList<>());
        });

        Mockito.when(lessonEventRepo.findLessonEventByDate(any(LocalDate.class))).then(invocation -> {
            LocalDate date = invocation.getArgument(0);
            String dateKey = date.toString();
            return Optional.ofNullable(lessonEventByDateStore.get(dateKey)).orElse(new ArrayList<>());
        });

        Mockito.when(lessonEventRepo.findLessonEventByTeacherAndDate(any(String.class), any(LocalDate.class))).then(invocation -> {
            String teacher = invocation.getArgument(0);
            LocalDate date = invocation.getArgument(1);
            String teacherAndDateKey = teacher + "_" + date.toString();
            return Optional.ofNullable(lessonEventByTeacherAndDateStore.get(teacherAndDateKey)).orElse(new ArrayList<>());
        });

        Mockito.doAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            LessonEvent lesson = lessonEventStore.remove(id);
            if (lesson != null) {
                lessonEventByStudentStore.get(lesson.getStudent()).remove(lesson);
                lessonEventByTeacherStore.get(lesson.getTeacher()).remove(lesson);
                String teacherAndDateKey = lesson.getTeacher() + "_" + lesson.getDate().toLocalDate().toString();
                lessonEventByTeacherAndDateStore.get(teacherAndDateKey).remove(lesson);
                lessonEventByDateStore.get(lesson.getDate().toLocalDate().toString()).remove(lesson);
            }
            return null;
        }).when(this.lessonEventRepo).deleteById(any());
    }


}
