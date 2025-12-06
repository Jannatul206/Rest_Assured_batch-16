package com.dailyfinance.tests;

import com.dailyfinance.base.BaseTest;
import com.dailyfinance.endpoints.AuthEndpoints;
import com.dailyfinance.models.LoginRequest;
import com.dailyfinance.models.RegisterRequest;
import com.dailyfinance.utils.ConfigReader;
import com.dailyfinance.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Daily Finance API")
@Feature("Authentication")
public class AuthenticationTest extends BaseTest {
    private AuthEndpoints authEndpoints;
    private RegisterRequest validRegisterRequest;
    private String testUserEmail;
    private String testUserPassword;
    
    @BeforeClass
    public void setupAuth() {
        authEndpoints = new AuthEndpoints();
        
        // Generate test data
        testUserEmail = "testuser" + System.currentTimeMillis() + "@example.com";
        testUserPassword = "Test@12345";
        
        validRegisterRequest = RegisterRequest.builder()
                .firstName(TestDataGenerator.generateFirstName())
                .lastName(TestDataGenerator.generateLastName())
                .email(testUserEmail)
                .password(testUserPassword)
                .phoneNumber(TestDataGenerator.generatePhoneNumber())
                .address(TestDataGenerator.generateAddress())
                .gender("Female")
                .termsAccepted(true)
                .build();
    }
    
    @Test(priority = 1)
    @Story("User Registration")
    @Description("Test successful user registration with valid data")
    @Severity(SeverityLevel.CRITICAL)
    public void testRegisterNewUser() {
        Response response = authEndpoints.registerUser(getRequestSpec(), validRegisterRequest);
        
        response.then()
                .statusCode(201)
                .log().all();
        
        // Extract and save user ID and token
        createdUserId = authEndpoints.extractUserId(response);
        userToken = authEndpoints.extractToken(response);
        
        Assert.assertNotNull(createdUserId, "User ID should not be null");
        Assert.assertNotNull(userToken, "Token should not be null");
        Assert.assertEquals(response.jsonPath().getString("email"), testUserEmail);
        Assert.assertEquals(response.jsonPath().getString("role"), "user");
    }
    
    @Test(priority = 2)
    @Story("User Registration")
    @Description("Test registration with duplicate email")
    @Severity(SeverityLevel.NORMAL)
    public void testRegisterWithDuplicateEmail() {
        Response response = authEndpoints.registerUser(getRequestSpec(), validRegisterRequest);
        
        response.then()
                .statusCode(400)
                .log().all();
        
        // Verify error message exists
        Assert.assertNotNull(response.getBody().asString());
    }
    
    @Test(priority = 3)
    @Story("User Registration")
    @Description("Test registration with missing required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testRegisterWithMissingFields() {
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .email(TestDataGenerator.generateEmail())
                .password("123")
                .build();
        
        Response response = authEndpoints.registerUser(getRequestSpec(), invalidRequest);
        
        // API returns 500 for missing fields instead of 400
        response.then()
                .statusCode(400)
                .log().all();
    }
    
    @Test(priority = 4)
    @Story("User Registration")
    @Description("Test registration with invalid email format")
    @Severity(SeverityLevel.NORMAL)
    public void testRegisterWithInvalidEmail() {
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email("invalidemail")
                .password("Test@12345")
                .phoneNumber("01712345678")
                .address("Dhaka")
                .gender("Male")
                .termsAccepted(true)
                .build();
        
        Response response = authEndpoints.registerUser(getRequestSpec(), invalidRequest);
        
        response.then()
                .statusCode(400)
                .log().all();
    }
    
    @Test(priority = 5)
    @Story("Admin Login")
    @Description("Test successful admin login")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdminLogin() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(ConfigReader.getAdminEmail())
                .password(ConfigReader.getAdminPassword())
                .build();
        
        Response response = authEndpoints.loginUser(getRequestSpec(), loginRequest);
        
        response.then()
                .statusCode(200)
                .log().all();
        
        adminToken = authEndpoints.extractToken(response);
        
        Assert.assertNotNull(adminToken, "Admin token should not be null");
        Assert.assertEquals(response.jsonPath().getString("role"), "admin");
        Assert.assertEquals(response.jsonPath().getString("email"), ConfigReader.getAdminEmail());
    }
    
    @Test(priority = 6)
    @Story("User Login")
    @Description("Test successful user login")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserLogin() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(testUserEmail)
                .password(testUserPassword)
                .build();
        
        Response response = authEndpoints.loginUser(getRequestSpec(), loginRequest);
        
        response.then()
                .statusCode(200)
                .log().all();
        
        String token = authEndpoints.extractToken(response);
        
        Assert.assertNotNull(token, "User token should not be null");
        Assert.assertEquals(response.jsonPath().getString("email"), testUserEmail);
        Assert.assertEquals(response.jsonPath().getString("role"), "user");
    }
    
    @Test(priority = 7)
    @Story("User Login")
    @Description("Test login with invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithInvalidCredentials() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(testUserEmail)
                .password("WrongPassword123")
                .build();
        
        Response response = authEndpoints.loginUser(getRequestSpec(), loginRequest);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 8)
    @Story("User Login")
    @Description("Test login with non-existent email")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithNonExistentEmail() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("Password123")
                .build();
        
        Response response = authEndpoints.loginUser(getRequestSpec(), loginRequest);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 9)
    @Story("User Login")
    @Description("Test login with empty credentials")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithEmptyCredentials() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("")
                .password("")
                .build();
        
        Response response = authEndpoints.loginUser(getRequestSpec(), loginRequest);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
}
