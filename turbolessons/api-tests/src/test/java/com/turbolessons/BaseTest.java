package com.turbolessons;


import com.turbolessons.config.AppConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = AppConfig.getBaseUrl();
        String accessToken = obtainAccessToken(); // Use client credentials flow here
        RestAssured.requestSpecification = new RequestSpecBuilder().addHeader("Authorization",
                                                                              "Bearer " + accessToken)
                .setContentType(ContentType.JSON)
                .build();

        RestAssured.filters(new RequestLoggingFilter(),
                            new ResponseLoggingFilter(),
                            new AllureRestAssured());
    }

    private static String obtainAccessToken() {
        // send a request to Okta's token endpoint and returning the token.
        return "generated-token-string";
    }
}
