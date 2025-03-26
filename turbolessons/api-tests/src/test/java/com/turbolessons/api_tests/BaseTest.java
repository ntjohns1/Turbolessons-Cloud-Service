package com.turbolessons.api_tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class BaseTest {

    protected static RequestSpecification requestSpec;

    @BeforeAll
    public static void setup() {

        // Obtain a valid JWT from Okta
        String accessToken = obtainAccessToken();

        // Set up a default request specification that adds the Authorization header
        requestSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .setContentType(ContentType.JSON)
                .build();

        // Set this as the default specification for all REST Assured requests
        RestAssured.requestSpecification = requestSpec;
    }

    private static String obtainAccessToken() {
        // TODO: Implement the token retrieval logic from Okta's token endpoint using client credentials flow.
        return "eyJraWQiOiJrZWF0aCIsInR5cCI6IkpXVCJ9..."; // Example token; replace with an actual token.
    }
}