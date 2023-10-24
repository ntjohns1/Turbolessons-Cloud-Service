package com.noslen.eventservice.service;

import com.noslen.eventservice.dao.LessonEventRepo;
import com.noslen.eventservice.dto.LessonEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public class LessonEventServiceTests {

    private LessonEventRepo lessonEventRepo;
    private LessonEventService service;
    Map<Integer, LessonEvent> lessonEventStore;
    Map<String, List<LessonEvent>> lessonEventByStudentStore;
    Map<String, List<LessonEvent>> lessonEventByTeacherStore;
    Map<String, List<LessonEvent>> lessonEventByDateStore;
    Map<String, List<LessonEvent>> lessonEventByTeacherAndDateStore;

    LocalDateTime start1 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            15,
                                            0);
    LocalDateTime end1 = start1.plusMinutes(30L);
    LocalDateTime start2 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            15,
                                            30);
    LocalDateTime end2 = start2.plusMinutes(30L);
    LocalDateTime start3 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            16,
                                            0);
    LocalDateTime end3 = start3.plusHours(1L);
    LocalDateTime start4 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            18,
                                            15,
                                            30);
    LocalDateTime end4 = start4.plusMinutes(30L);

    String title1 = "StudentA - 3:00PM";
    String title2 = "StudentB - 3:00PM"; //8/18
    String title3 = "StudentC - 3:00PM";
    String title4 = "StudentD - 4:00PM";
    String student1 = "StudentA";
    String student2 = "StudentB";
    String student3 = "StudentC";
    String student4 = "StudentD";
    String teacher1 = "TeacherA";
    String teacher2 = "TeacherB";
    String studentEmail1 = "student_a@example.com";
    String studentEmail2 = "student_b@example.com";
    String studentEmail3 = "student_c@example.com";
    String studentEmail4 = "student_d@example.com";
    String teacherEmail1 = "teacher_a@example.com";
    String teacherEmail2 = "teacher_b@example.com";
    String comments = "test test";

    LessonEvent lesson;

    @BeforeEach
    public void setUp() {
        lessonEventStore = new HashMap<>();
        lessonEventByStudentStore = new HashMap<>();
        lessonEventByTeacherStore = new HashMap<>();
        lessonEventByDateStore = new HashMap<>();
        lessonEventByTeacherAndDateStore = new HashMap<>();
        setUpLessonEventRepoMock();
        service = new LessonEventService(lessonEventRepo);
        lesson = new LessonEvent(start1,
                                 end1,
                                 title1,
                                 student1,
                                 studentEmail1,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        lesson = new LessonEvent(start1,
                                 end1,
                                 title1,
                                 student1,
                                 studentEmail1,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        service.saveLessonEvent(lesson);
        lesson = new LessonEvent(start2,
                                 end2,
                                 title2,
                                 student2,
                                 studentEmail2,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        service.saveLessonEvent(lesson);
        lesson = new LessonEvent(start3,
                                 end3,
                                 title3,
                                 student3,
                                 studentEmail3,
                                 teacher2,
                                 teacherEmail2,
                                 comments);
        service.saveLessonEvent(lesson);
        lesson = new LessonEvent(start4,
                                 end4,
                                 title4,
                                 student4,
                                 studentEmail4,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        service.saveLessonEvent(lesson);
    }

    //Get All Lesson Event Events
    @Test
    public void testFindAllLessonEvents() {

        System.out.println("****** Map Values: \n" + lessonEventStore.values());

        List<LessonEvent> allLessons = service.findAllLessonEvents();
        Assertions.assertEquals(4,
                                allLessons.size());
        Assertions.assertEquals(start1,
                                allLessons.get(0)
                                        .getStartTime());
        Assertions.assertEquals(end1,
                                allLessons.get(0)
                                        .getEndTime());
        Assertions.assertEquals(title1,
                                allLessons.get(0)
                                        .getTitle());
        Assertions.assertEquals(student1,
                                allLessons.get(0)
                                        .getStudent());
        Assertions.assertEquals(studentEmail1,
                                allLessons.get(0)
                                        .getStudentEmail());
        Assertions.assertEquals(teacher1,
                                allLessons.get(0)
                                        .getTeacher());
        Assertions.assertEquals(teacherEmail1,
                                allLessons.get(0)
                                        .getTeacherEmail());
        Assertions.assertEquals(start1.toLocalDate(),
                                allLessons.get(0)
                                        .getDate());
        Assertions.assertEquals(comments,
                                allLessons.get(0)
                                        .getComments());
        Assertions.assertEquals(start3,
                                allLessons.get(2)
                                        .getStartTime());
        Assertions.assertEquals(end3,
                                allLessons.get(2)
                                        .getEndTime());
        Assertions.assertEquals(title3,
                                allLessons.get(2)
                                        .getTitle());
        Assertions.assertEquals(student3,
                                allLessons.get(2)
                                        .getStudent());
        Assertions.assertEquals(studentEmail3,
                                allLessons.get(2)
                                        .getStudentEmail());
        Assertions.assertEquals(teacher2,
                                allLessons.get(2)
                                        .getTeacher());
        Assertions.assertEquals(teacherEmail2,
                                allLessons.get(2)
                                        .getTeacherEmail());
        Assertions.assertEquals(start3.toLocalDate(),
                                allLessons.get(2)
                                        .getDate());
        Assertions.assertEquals(comments,
                                allLessons.get(2)
                                        .getComments());

        lessonEventStore.clear();
    }

    //Get One Lesson Event
    @Test
    public void shouldSaveFindDeleteLessonEvent() {
        LessonEvent fromService = service.findLessonEvent(lesson.getId());
        Assertions.assertEquals(lesson,
                                fromService);
        service.deleteLessonEvent(lesson.getId());
        fromService = service.findLessonEvent(lesson.getId());
        Assertions.assertNull(fromService);
        lessonEventStore.clear();
    }

    //Get Lesson Event Events By Teacher
    @Test
    public void shouldFindLessonEventByTeacher() {

        List<LessonEvent> fromService = service.findLessonEventsByTeacher(teacher1);
        Assertions.assertEquals(3,
                                fromService.size());
        Assertions.assertEquals(teacher1,
                                fromService.get(0)
                                        .getTeacher());
        lessonEventByTeacherStore.clear();
    }

    //Get Lesson Event Events By Student
    @Test
    public void shouldFindLessonEventByStudent() {

        List<LessonEvent> fromService = service.findLessonEventsByStudent(student1);
        Assertions.assertEquals(1,
                                fromService.size());
        Assertions.assertEquals(student1,
                                fromService.get(0)
                                        .getStudent());
        lessonEventByStudentStore.clear();
    }

    //Get Lesson Event Events By Teacher and Date
    @Test
    public void shouldFindLessonEventByDate() {
        // Create and save 3 lessons with the same teacher and date

        // Call the method we want to test
        List<LessonEvent> fromService = service.findLessonEventsByDate(start1.toLocalDate());

        // Assert that the result is as expected
        Assertions.assertEquals(3,
                                fromService.size());
        Assertions.assertEquals(teacher1,
                                fromService.get(0)
                                        .getTeacher());
        Assertions.assertEquals(start1.toLocalDate(),
                                fromService.get(0)
                                        .getDate());
        lessonEventByDateStore.clear();
    }


    @Test
    public void shouldFindLessonEventByTeacherAndDate() {

        // Call the method we want to test
        List<LessonEvent> fromService = service.findLessonEventsByTeacherAndDate(teacher1,
                                                                                 start1.toLocalDate());

        // Assert that the result is as expected
        Assertions.assertEquals(2,
                                fromService.size());
        Assertions.assertEquals(teacher1,
                                fromService.get(0)
                                        .getTeacher());
        Assertions.assertEquals(start1.toLocalDate(),
                                fromService.get(0)
                                        .getDate());
        lessonEventByTeacherAndDateStore.clear();
    }

    //Update Lesson Event
    @Test
    public void shouldUpdateLessonEvent() {
        LocalDateTime updatedTime = LocalDateTime.of(2033,
                                                     Month.AUGUST,
                                                     19,
                                                     15,
                                                     0);
        LessonEvent updatedLesson = new LessonEvent(updatedTime,
                                                    updatedTime.plusMinutes(30L),
                                                    "updatedTitle",
                                                    "updatedStudent",
                                                    "updated_s@example.com",
                                                    "updatedTeacher",
                                                    "updated_t@example.com",
                                                    "updatedComments");
        service.updateLessonEvent(lesson.getId(),
                                  updatedLesson);
        LessonEvent updatedFromService = service.findLessonEvent(lesson.getId());
        Assertions.assertEquals(updatedLesson.getStartTime(),
                                updatedFromService.getStartTime());
        Assertions.assertEquals(updatedLesson.getEndTime(),
                                updatedFromService.getEndTime());
        Assertions.assertEquals(updatedLesson.getTitle(),
                                updatedFromService.getTitle());
        Assertions.assertEquals(updatedLesson.getStudent(),
                                updatedFromService.getStudent());
        Assertions.assertEquals(updatedLesson.getStudentEmail(),
                                updatedFromService.getStudentEmail());
        Assertions.assertEquals(updatedLesson.getTeacher(),
                                updatedFromService.getTeacher());
        Assertions.assertEquals(updatedLesson.getTeacherEmail(),
                                updatedFromService.getTeacherEmail());
        Assertions.assertEquals(updatedLesson.getDate(),
                                updatedFromService.getDate());
        Assertions.assertEquals(updatedLesson.getComments(),
                                updatedFromService.getComments());
        lessonEventStore.clear();
    }

    private void setUpLessonEventRepoMock() {

        lessonEventRepo = mock(LessonEventRepo.class);

        Mockito.when(lessonEventRepo.findAll())
                .thenAnswer(invocation -> new ArrayList<>(lessonEventStore.values()));

        Mockito.when(lessonEventRepo.save(any(LessonEvent.class)))
                .then(invocation -> {
                    LessonEvent lesson = invocation.getArgument(0);
                    int newId = lessonEventStore.size() + 1;
                    lesson.setId(newId);
                    lessonEventStore.put(newId,
                                         lesson);
                    lessonEventByStudentStore.computeIfAbsent(lesson.getStudent(),
                                                              k -> new ArrayList<>())
                            .add(lesson);
                    lessonEventByTeacherStore.computeIfAbsent(lesson.getTeacher(),
                                                              k -> new ArrayList<>())
                            .add(lesson);
                    String teacherAndDateKey = lesson.getTeacher() + "_" + lesson.getDate()
                            .toString();
                    lessonEventByTeacherAndDateStore.computeIfAbsent(teacherAndDateKey,
                                                                     k -> new ArrayList<>())
                            .add(lesson);
                    lessonEventByDateStore.computeIfAbsent(lesson.getDate()
                                                                   .toString(),
                                                           k -> new ArrayList<>())
                            .add(lesson);
                    return lesson;
                });

        Mockito.when(lessonEventRepo.findById(any(Integer.class)))
                .then(invocation -> {
                    Integer id = invocation.getArgument(0);
                    return Optional.ofNullable(lessonEventStore.get(id));
                });

        Mockito.when(lessonEventRepo.findLessonEventByStudent(any(String.class)))
                .then(invocation -> {
                    String student = invocation.getArgument(0);
                    return Optional.ofNullable(lessonEventByStudentStore.get(student))
                            .orElse(new ArrayList<>());
                });

        Mockito.when(lessonEventRepo.findLessonEventByTeacher(any(String.class)))
                .then(invocation -> {
                    String teacher = invocation.getArgument(0);
                    return Optional.ofNullable(lessonEventByTeacherStore.get(teacher))
                            .orElse(new ArrayList<>());
                });

        Mockito.when(lessonEventRepo.findLessonEventByDate(any(LocalDate.class)))
                .then(invocation -> {
                    LocalDate date = invocation.getArgument(0);
                    String dateKey = date.toString();
                    return Optional.ofNullable(lessonEventByDateStore.get(dateKey))
                            .orElse(new ArrayList<>());
                });

        Mockito.when(lessonEventRepo.findLessonEventByTeacherAndDate(any(String.class),
                                                                     any(LocalDate.class)))
                .then(invocation -> {
                    String teacher = invocation.getArgument(0);
                    LocalDate date = invocation.getArgument(1);
                    String teacherAndDateKey = teacher + "_" + date.toString();
                    return Optional.ofNullable(lessonEventByTeacherAndDateStore.get(teacherAndDateKey))
                            .orElse(new ArrayList<>());
                });

        Mockito.doAnswer(invocation -> {
                    Integer id = invocation.getArgument(0);
                    LessonEvent lesson = lessonEventStore.remove(id);
                    if (lesson != null) {
                        lessonEventByStudentStore.get(lesson.getStudent())
                                .remove(lesson);
                        lessonEventByTeacherStore.get(lesson.getTeacher())
                                .remove(lesson);
                        String teacherAndDateKey = lesson.getTeacher() + "_" + lesson.getDate()
                                .toString();
                        lessonEventByTeacherAndDateStore.get(teacherAndDateKey)
                                .remove(lesson);
                        lessonEventByDateStore.get(lesson.getDate()
                                                           .toString())
                                .remove(lesson);
                    }
                    return null;
                })
                .when(this.lessonEventRepo)
                .deleteById(any());
    }


}
