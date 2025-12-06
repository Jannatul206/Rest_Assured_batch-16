package com.dailyfinance.endpoints;

import com.dailyfinance.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserEndpoints {
    private static final String USERS_ENDPOINT = "/user/users";
    private static final String USER_BY_ID_ENDPOINT = "/user/{userId}";
    
    @Step("Get all users")
    public Response getAllUsers(RequestSpecification requestSpec) {
        return given()
                .spec(requestSpec)
                .when()
                .get(USERS_ENDPOINT);
    }
    
    @Step("Get user by ID: {userId}")
    public Response getUserById(RequestSpecification requestSpec, String userId) {
        return given()
                .spec(requestSpec)
                .pathParam("userId", userId)
                .when()
                .get(USER_BY_ID_ENDPOINT);
    }
    
    @Step("Update user with ID: {userId}")
    public Response updateUser(RequestSpecification requestSpec, String userId, User user) {
        return given()
                .spec(requestSpec)
                .pathParam("userId", userId)
                .body(user)
                .when()
                .put(USER_BY_ID_ENDPOINT);
    }
    
    @Step("Delete user with ID: {userId}")
    public Response deleteUser(RequestSpecification requestSpec, String userId) {
        return given()
                .spec(requestSpec)
                .pathParam("userId", userId)
                .when()
                .delete(USER_BY_ID_ENDPOINT);
    }
}
