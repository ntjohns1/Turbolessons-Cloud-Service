package com.turbolessons.api_tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class LessonEventControllerTest extends BaseTest {

    private static final String BASE_URL = "https://www.turbolessons.com";
    private static final String LESSONS_ENDPOINT = "/api/lessons";

    @Test
    void getAllLessons_ShouldReturnLessonsList() {
        given()
            .spec(requestSpec)
            .baseUri(BASE_URL)
        .when()
            .get(LESSONS_ENDPOINT)
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("$", hasSize(greaterThanOrEqualTo(0))); // Verify we get an array of any size
    }

    @Test
    void createLesson_ShouldReturnCreatedLesson() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);

        Map<String, Object> lessonData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Test Piano Lesson",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", "Test Teacher",
            "teacherEmail", "test.teacher@example.com",
            "comments", "Test lesson created by API test",
            "billingStatus", "UNLOGGED"
        );

        given()
            .spec(requestSpec)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(LESSONS_ENDPOINT)
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("title", equalTo(lessonData.get("title")))
            .body("student", equalTo(lessonData.get("student")))
            .body("teacher", equalTo(lessonData.get("teacher")))
            .body("id", notNullValue());
    }

    @Test
    void getLessonById_ShouldReturnLesson() {
        // First create a lesson
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);

        Map<String, Object> lessonData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Test Piano Lesson",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", "Test Teacher",
            "teacherEmail", "test.teacher@example.com",
            "comments", "Test lesson created by API test",
            "billingStatus", "UNLOGGED"
        );

        Integer lessonId = given()
            .spec(requestSpec)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(LESSONS_ENDPOINT)
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .path("id");

        // Then retrieve it by ID
        given()
            .spec(requestSpec)
            .baseUri(BASE_URL)
        .when()
            .get(LESSONS_ENDPOINT + "/{id}", lessonId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("id", equalTo(lessonId))
            .body("title", equalTo(lessonData.get("title")))
            .body("student", equalTo(lessonData.get("student")))
            .body("teacher", equalTo(lessonData.get("teacher")));
    }

    @Test
    void updateLesson_ShouldUpdateExistingLesson() {
        // First create a lesson
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);

        Map<String, Object> lessonData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Original Title",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", "Test Teacher",
            "teacherEmail", "test.teacher@example.com",
            "comments", "Original comments",
            "billingStatus", "UNLOGGED"
        );

        Integer lessonId = given()
            .spec(requestSpec)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(LESSONS_ENDPOINT)
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .path("id");

        // Update the lesson
        Map<String, Object> updatedData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Updated Title",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", "Test Teacher",
            "teacherEmail", "test.teacher@example.com",
            "comments", "Updated comments",
            "billingStatus", "UNLOGGED"
        );

        given()
            .spec(requestSpec)
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(updatedData)
        .when()
            .put(LESSONS_ENDPOINT + "/{id}", lessonId)
        .then()
            .statusCode(HttpStatus.OK.value());

        // Verify the update
        given()
            .spec(requestSpec)
            .baseUri(BASE_URL)
        .when()
            .get(LESSONS_ENDPOINT + "/{id}", lessonId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("id", equalTo(lessonId))
            .body("title", equalTo("Updated Title"))
            .body("comments", equalTo("Updated comments"));
    }
}
