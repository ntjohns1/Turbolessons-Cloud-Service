package com.noslen.eventservice.controller;

import com.noslen.eventservice.dto.LessonEvent;
import com.noslen.eventservice.service.LessonEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonEventController.class)
@TestPropertySource(
        locations = "classpath:application-test.yml",
        properties = { "spring.config.name=application-test" }
)
@ActiveProfiles("test")
public class LessonEventControllerTests {

    @MockBean
    private LessonEventService service;

    @Autowired
    private MockMvc mockMvc;

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
    LessonEvent lesson1;
    LessonEvent lesson2;
    LessonEvent lesson3;

    @BeforeEach
    void setUp() {
        lesson1 = new LessonEvent(start1,
                                 end1,
                                 title1,
                                 student1,
                                 studentEmail1,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        lesson1.setId(1);
        lesson2 = new LessonEvent(start2,
                                 end2,
                                 title2,
                                 student2,
                                 studentEmail2,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        lesson2.setId(2);
        lesson3 = new LessonEvent(start3,
                                 end3,
                                 title3,
                                 student3,
                                 studentEmail3,
                                 teacher2,
                                 teacherEmail2,
                                 comments);
        lesson3.setId(3);
    }

    @Test
    void shouldReturnAllLessons() throws Exception {
        Mockito.when(service.findAllLessonEvents())
                .thenReturn(Collections.singletonList(lesson1));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        mockMvc.perform(get("/api/lessons").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startTime").value(lesson1.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(lesson1.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].student").value(lesson1.getStudent()))
                .andExpect(jsonPath("$[0].studentEmail").value(lesson1.getStudentEmail()))
                .andExpect(jsonPath("$[0].teacher").value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[0].teacherEmail").value(lesson1.getTeacherEmail()))
                .andExpect(jsonPath("$[0].date").value(lesson1.getDate().toString()))
                .andExpect(jsonPath("$[0].comments").value(lesson1.getComments()));
    }

    @Test
    void shouldReturnLessonById() throws Exception {
        Mockito.when(service.findLessonEvent(any()))
                .thenReturn(lesson1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        mockMvc.perform(get("/api/lessons/1").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value(lesson1.getStartTime().format(formatter)))
                .andExpect(jsonPath("$.endTime").value(lesson1.getEndTime().format(formatter)))
                .andExpect(jsonPath("$.student").value(lesson1.getStudent()))
                .andExpect(jsonPath("$.studentEmail").value(lesson1.getStudentEmail()))
                .andExpect(jsonPath("$.teacher").value(lesson1.getTeacher()))
                .andExpect(jsonPath("$.teacherEmail").value(lesson1.getTeacherEmail()))
                .andExpect(jsonPath("$.date").value(lesson1.getDate().toString()))
                .andExpect(jsonPath("$.comments").value(lesson1.getComments()));
    }

    @Test
    void shouldReturnLessonByStudent() throws Exception {
        Mockito.when(service.findLessonEventsByStudent(any()))
                .thenReturn(Arrays.asList(lesson1,
                                          lesson2));
        mockMvc.perform(get("/api/lessons/student/StudentA").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student").value(lesson1.getStudent()));
    }

    @Test
    void shouldReturnLessonByTeacher() throws Exception {
        Mockito.when(service.findLessonEventsByTeacher(any()))
                .thenReturn(Arrays.asList(lesson1,
                                          lesson2));
        mockMvc.perform(get("/api/lessons/teacher/TeacherA").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher").value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[1].teacher").value(lesson2.getTeacher()));
    }

    @Test
    void shouldReturnLessonByDate() throws Exception {
        Mockito.when(service.findLessonEventsByDate(any()))
                .thenReturn(Arrays.asList(lesson1,
                                          lesson2));
        mockMvc.perform(get("/api/lessons/date/2033-08-17").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher").value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[0].student").value(lesson1.getStudent()))
                .andExpect(jsonPath("$[1].teacher").value(lesson2.getTeacher()))
                .andExpect(jsonPath("$[1].student").value(lesson2.getStudent()));
    }

    @Test
    void shouldReturnLessonByTeacherAndDate() throws Exception {
        Mockito.when(service.findLessonEventsByTeacherAndDate(any(),
                                                              any()))
                .thenReturn(Arrays.asList(lesson1,
                                          lesson2));
        mockMvc.perform(get("/api/lessons/TeacherA/2033-08-17").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher").value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[1].teacher").value(lesson2.getTeacher()));
    }

    @Test
    void shouldCreateLesson() throws Exception {
        Mockito.when(service.saveLessonEvent(any()))
                .thenReturn(lesson1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String lessonContent = String.format(
                "{" +
                        "\"startTime\":\"%s\"," +
                        "\"endTime\":\"%s\"," +
                        "\"title\":\"%s\"," +
                        "\"student\":\"%s\"," +
                        "\"studentEmail\":\"%s\"," +
                        "\"teacher\":\"%s\"," +
                        "\"teacherEmail\":\"%s\"," +
                        "\"comments\":\"%s\"" +
                        "}",
                start1.format(formatter),  // Using the DateTimeFormatter you've defined
                end1.format(formatter),
                title1,
                student1,
                studentEmail1,
                teacher1,
                teacherEmail1,
                comments
        );

        mockMvc.perform(post("/api/lessons").with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(lessonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").hasJsonPath());
    }

    @Test
    void shouldUpdateLesson() throws Exception {
        Mockito.when(service.findLessonEvent(any()))
                .thenReturn(lesson1);
        ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String lessonContent = String.format(
                "{" +
                        "\"startTime\":\"%s\"," +
                        "\"endTime\":\"%s\"," +
                        "\"title\":\"%s\"," +
                        "\"student\":\"%s\"," +
                        "\"studentEmail\":\"%s\"," +
                        "\"teacher\":\"%s\"," +
                        "\"teacherEmail\":\"%s\"," +
                        "\"comments\":\"%s\"" +
                        "}",
                start1.plusDays(1L).format(formatter),  // Using the DateTimeFormatter you've defined
                end1.plusDays(1L).format(formatter),
                "updated title",
                "updatedStudent",
                "updated_s@example.com",
                "updatedTeacher",
                "updated_t@example.com",
                "updatedComments"
        );
        mockMvc.perform(put("/api/lessons/1").with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(lessonContent))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteLessonEvent() throws Exception {
        Mockito.doNothing()
                .when(service)
                .deleteLessonEvent(any());
        mockMvc.perform(delete("/api/lessons/1").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

