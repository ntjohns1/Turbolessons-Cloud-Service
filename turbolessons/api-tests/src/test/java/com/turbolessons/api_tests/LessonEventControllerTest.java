package com.turbolessons.api_tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public class LessonEventControllerTest extends BaseTest {

    @Value("${event-service.url}")
    private String baseUrl;

    @Value("${event-service.endpoints.lessons}")
    private String lessonsEndpoint;

    @Test
    void getAllLessons_ShouldReturnLessonsList() {
        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
        .when()
            .get(lessonsEndpoint)
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
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(lessonsEndpoint)
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
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(lessonsEndpoint)
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .path("id");

        // Then retrieve it by ID
        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
        .when()
            .get(lessonsEndpoint + "/{id}", lessonId)
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
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(lessonsEndpoint)
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .path("id");

        // Update the lesson
        Map<String, Object> updatedData = Map.of(
            "id", lessonId,
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
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(updatedData)
        .when()
            .put(lessonsEndpoint + "/{id}", lessonId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("id", equalTo(lessonId))
            .body("title", equalTo(updatedData.get("title")))
            .body("comments", equalTo(updatedData.get("comments")));
    }
    
    @Test
    void getLessonsByBillingStatus_ShouldReturnFilteredLessons() {
        // First create a lesson with LOGGED status
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);

        Map<String, Object> lessonData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Billable Lesson",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", "Test Teacher",
            "teacherEmail", "test.teacher@example.com",
            "comments", "This lesson should be billed",
            "billingStatus", "LOGGED"
        );

        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(lessonsEndpoint)
        .then()
            .statusCode(HttpStatus.OK.value());

        // Query lessons by billing status
        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
        .when()
            .get(lessonsEndpoint + "/billing/LOGGED")
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("$", hasSize(greaterThanOrEqualTo(1)))
            .body("find { it.billingStatus == 'LOGGED' }", notNullValue());
    }
    
    @Test
    void getLessonsByTeacherAndBillingStatus_ShouldReturnFilteredLessons() {
        // First create a lesson with specific teacher and LOGGED status
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);
        String teacher = "TeacherForBilling";

        Map<String, Object> lessonData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Teacher Billing Test",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", teacher,
            "teacherEmail", "teacher.billing@example.com",
            "comments", "This lesson should be billed for specific teacher",
            "billingStatus", "LOGGED"
        );

        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(lessonsEndpoint)
        .then()
            .statusCode(HttpStatus.OK.value());

        // Query lessons by teacher and billing status
        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
        .when()
            .get(lessonsEndpoint + "/teacher/{teacher}/billing/{status}", teacher, "LOGGED")
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("$", hasSize(greaterThanOrEqualTo(1)))
            .body("find { it.teacher == '" + teacher + "' && it.billingStatus == 'LOGGED' }", notNullValue());
    }
    
    @Test
    void getLessonsByDateRangeAndBillingStatus_ShouldReturnFilteredLessons() {
        // First create a lesson with specific date and LOGGED status
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);
        LocalDate lessonDate = startTime.toLocalDate();
        
        Map<String, Object> lessonData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Date Range Billing Test",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", "Test Teacher",
            "teacherEmail", "teacher@example.com",
            "comments", "This lesson should be found in date range query",
            "billingStatus", "LOGGED"
        );

        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(lessonsEndpoint)
        .then()
            .statusCode(HttpStatus.OK.value());

        // Query lessons by date range and billing status
        LocalDate startDate = lessonDate.minusDays(1);
        LocalDate endDate = lessonDate.plusDays(1);
        
        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
            .queryParam("startDate", startDate.toString())
            .queryParam("endDate", endDate.toString())
        .when()
            .get(lessonsEndpoint + "/billing/{status}/daterange", "LOGGED")
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("$", hasSize(greaterThanOrEqualTo(1)))
            .body("find { it.billingStatus == 'LOGGED' }", notNullValue());
    }
    
    @Test
    void updateLessonBillingStatus_ShouldUpdateOnlyBillingStatus() {
        // First create a lesson with UNLOGGED status
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);

        Map<String, Object> lessonData = Map.of(
            "startTime", startTime.toString(),
            "endTime", endTime.toString(),
            "title", "Billing Status Update Test",
            "student", "Test Student",
            "studentEmail", "test.student@example.com",
            "teacher", "Test Teacher",
            "teacherEmail", "teacher@example.com",
            "comments", "This lesson's billing status will be updated",
            "billingStatus", "UNLOGGED"
        );

        Integer lessonId = given()
            .spec(requestSpec)
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(lessonData)
        .when()
            .post(lessonsEndpoint)
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .path("id");

        // Update just the billing status
        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
        .when()
            .patch(lessonsEndpoint + "/{id}/billing/{status}", lessonId, "LOGGED")
        .then()
            .statusCode(HttpStatus.OK.value());
            
        // Verify the billing status was updated
        given()
            .spec(requestSpec)
            .baseUri(baseUrl)
        .when()
            .get(lessonsEndpoint + "/{id}", lessonId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("id", equalTo(lessonId))
            .body("title", equalTo(lessonData.get("title")))
            .body("billingStatus", equalTo("LOGGED"));
    }
}
