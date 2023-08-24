package com.noslen.eventservice.controller;

import com.noslen.eventservice.dto.LessonEvent;
import com.noslen.eventservice.service.LessonEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(LessonEventController.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public class LessonEventControllerTests {

    @MockBean
    private LessonEventService service;

    @Autowired
    private MockMvc mockMvc;

    LocalDateTime date1 = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 0);
    LocalDateTime date2 = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 30);
    LocalDateTime date3 = LocalDateTime.of(2033, Month.AUGUST, 18, 15, 30);
    LessonEvent lesson1;
    LessonEvent lesson2;
    LessonEvent lesson3;

    @BeforeEach
    void setUp() {
        lesson1 = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date1,"Test");
        lesson2 = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date2,"Test");
        lesson3 = new LessonEvent("testStudent","teststudent@email.com","testTeacher","testteacher@email.com",date3,"Test");
    }

    @Test
    void shouldReturnAllLessons() throws Exception {
        Mockito.when(service.findAllLessonEvents()).thenReturn(Collections.singletonList(lesson1));
        mockMvc.perform(get("/api/lessons").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student")
                        .value(lesson1.getStudent()))
                .andExpect(jsonPath("$[0].teacher")
                        .value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[0].comments")
                        .value(lesson1.getComments()));
    }

    @Test
    void shouldReturnLessonById() throws Exception{
        Mockito.when(service.findLessonEvent(any())).thenReturn(lesson1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = lesson1.getDate().format(formatter);
        mockMvc.perform(get("/api/lessons/1").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student")
                        .value(lesson1.getStudent()))
                .andExpect(jsonPath("$.teacher")
                        .value(lesson1.getTeacher()))
                .andExpect(jsonPath("$.date")
                        .value(formattedDateTime))
                .andExpect(jsonPath("$.comments")
                        .value(lesson1.getComments()));
    }

    @Test
    void shouldReturnLessonByStudent() throws Exception {
        Mockito.when(service.findLessonEventsByStudent(any())).thenReturn(Arrays.asList(lesson1,lesson2));
        mockMvc.perform(get("/api/lessons/student/testStudent").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student")
                        .value(lesson1.getStudent()))
                .andExpect(jsonPath("$[1].student")
                        .value(lesson1.getStudent()));
    }

    @Test
    void shouldReturnLessonByTeacher() throws Exception {
        Mockito.when(service.findLessonEventsByTeacher(any())).thenReturn(Arrays.asList(lesson1,lesson2));
        mockMvc.perform(get("/api/lessons/teacher/testTeacher").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher")
                        .value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[1].teacher")
                        .value(lesson1.getTeacher()));
    }

    @Test
    void shouldReturnLessonByDate() throws Exception {
        Mockito.when(service.findLessonEventsByDate(any())).thenReturn(Arrays.asList(lesson1,lesson2));
        mockMvc.perform(get("/api/lessons/date/2033-08-17").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher")
                        .value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[0].student")
                        .value(lesson1.getStudent()))
                .andExpect(jsonPath("$[1].teacher")
                        .value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[1].student")
                .value(lesson1.getStudent()));
    }

    @Test
    void shouldReturnLessonByTeacherAndDate() throws Exception {
        Mockito.when(service.findLessonEventsByTeacherAndDate(any(),any())).thenReturn(Arrays.asList(lesson1,lesson2));
        mockMvc.perform(get("/api/lessons/testTeacher/2033-08-17").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher")
                        .value(lesson1.getTeacher()))
                .andExpect(jsonPath("$[1].teacher")
                        .value(lesson1.getTeacher()));
    }

    @Test
    void shouldCreateLesson() throws Exception {
        Mockito.when(service.saveLessonEvent(any())).thenReturn(lesson1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = lesson1.getDate().format(formatter);
        mockMvc.perform(post("/api/lessons").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"student\":\"testStudent\",\"teacher\":\"testTeacher\",\"date\":\""+formattedDateTime+"\",\"comments\":\"testComment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").hasJsonPath());
    }

    @Test
    void shouldUpdateLesson() throws Exception {
        Mockito.when(service.findLessonEvent(any())).thenReturn(lesson1);;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = lesson1.getDate().format(formatter);
        mockMvc.perform(put("/api/lessons/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"student\":\"updatedStudent\",\"teacher\":\"updatedTeacher\",\"date\":\""+formattedDateTime+"\",\"comments\":\"updatedComment\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteLessonEvent() throws Exception {
        Mockito.doNothing().when(service).deleteLessonEvent(any());
        mockMvc.perform(delete("/api/lessons/1").with(jwt()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
