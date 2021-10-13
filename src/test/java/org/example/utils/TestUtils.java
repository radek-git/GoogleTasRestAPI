package org.example.utils;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TestUtils {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String BEARER = "Bearer ";

    private TestUtils() {}

    public static RequestSpecification givenJsonRequestWithToken(String token) {
        return given().contentType(ContentType.JSON)
                .urlEncodingEnabled(false)
                .header(AUTHORIZATION_HEADER_NAME, BEARER + token);
    }
}
