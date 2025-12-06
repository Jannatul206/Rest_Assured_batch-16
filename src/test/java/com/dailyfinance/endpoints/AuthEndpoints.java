package com.dailyfinance.endpoints;

import com.dailyfinance.models.AuthResponse;
import com.dailyfinance.models.LoginRequest;
import com.dailyfinance.models.RegisterRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AuthEndpoints {
    private static final String REGISTER_ENDPOINT = "/auth/register";
    private static final String LOGIN_ENDPOINT = "/auth/login";
    
    @Step("Register new user with email: {request.email}")
    public Response registerUser(RequestSpecification requestSpec, RegisterRequest request) {
        return given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post(REGISTER_ENDPOINT);
    }
    
    @Step("Login user with email: {request.email}")
    public Response loginUser(RequestSpecification requestSpec, LoginRequest request) {
        return given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post(LOGIN_ENDPOINT);
    }
    
    @Step("Extract token from authentication response")
    public String extractToken(Response response) {
        AuthResponse authResponse = response.as(AuthResponse.class);
        return authResponse.getToken();
    }
    
    @Step("Extract user ID from authentication response")
    public String extractUserId(Response response) {
        AuthResponse authResponse = response.as(AuthResponse.class);
        return authResponse.getId();
    }
}
