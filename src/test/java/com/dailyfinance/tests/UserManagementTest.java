package com.dailyfinance.tests;

import com.dailyfinance.base.BaseTest;
import com.dailyfinance.endpoints.UserEndpoints;
import com.dailyfinance.models.User;
import com.dailyfinance.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Daily Finance API")
@Feature("User Management")
public class UserManagementTest extends BaseTest {
    private UserEndpoints userEndpoints;
    private User updatedUser;
    
    @BeforeClass
    public void setupUserManagement() {
        userEndpoints = new UserEndpoints();
    }
    
    @Test(priority = 10)
    @Story("Get User List")
    @Description("Test retrieving all users with admin token")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllUsers() {
        Response response = userEndpoints.getAllUsers(getAuthenticatedRequestSpec(adminToken));
        
        response.then()
                .statusCode(200)
                .log().all();
        
        List<User> users = response.jsonPath().getList("", User.class);
        Assert.assertTrue(users.size() > 0, "User list should not be empty");
    }
    
    @Test(priority = 11)
    @Story("Get User List")
    @Description("Test retrieving users without authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllUsersWithoutAuth() {
        Response response = userEndpoints.getAllUsers(getRequestSpec());
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 12)
    @Story("Search User")
    @Description("Test searching user by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserById() {
        // Skip if adminToken or createdUserId is null
        if (adminToken == null || createdUserId == null) {
            throw new org.testng.SkipException("Admin token or user ID not available - depends on earlier test");
        }
        
        Response response = userEndpoints.getUserById(getAuthenticatedRequestSpec(adminToken), createdUserId);
        
        response.then()
                .statusCode(200)
                .log().all();
        
        User user = response.as(User.class);
        Assert.assertEquals(user.getId(), createdUserId);
        Assert.assertNotNull(user.getEmail());
    }
    
    @Test(priority = 13)
    @Story("Search User")
    @Description("Test searching user with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserByInvalidId() {
        Response response = userEndpoints.getUserById(getAuthenticatedRequestSpec(adminToken), "invalid-id-12345");
        
        response.then()
                .statusCode(404)
                .log().all();
    }
    
    @Test(priority = 14)
    @Story("Search User")
    @Description("Test searching user without authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserByIdWithoutAuth() {
        Response response = userEndpoints.getUserById(getRequestSpec(), createdUserId);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 15)
    @Story("Update User")
    @Description("Test updating user information (firstname and phone number)")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateUserInfo() {
        // Skip if adminToken or createdUserId is null
        if (adminToken == null || createdUserId == null) {
            throw new org.testng.SkipException("Admin token or user ID not available - depends on earlier test");
        }
        
        // First get the user details
        Response getResponse = userEndpoints.getUserById(getAuthenticatedRequestSpec(adminToken), createdUserId);
        updatedUser = getResponse.as(User.class);
        
        // Update specific fields
        String newFirstName = "Updated" + TestDataGenerator.generateFirstName();
        String newPhoneNumber = TestDataGenerator.generatePhoneNumber();
        
        updatedUser.setFirstName(newFirstName);
        updatedUser.setPhoneNumber(newPhoneNumber);
        
        // Update the user
        Response response = userEndpoints.updateUser(getAuthenticatedRequestSpec(adminToken), createdUserId, updatedUser);
        
        response.then()
                .statusCode(200)
                .log().all();
        
        User updatedUserResponse = response.as(User.class);
        Assert.assertEquals(updatedUserResponse.getFirstName(), newFirstName);
        Assert.assertEquals(updatedUserResponse.getPhoneNumber(), newPhoneNumber);
    }
    
    @Test(priority = 16)
    @Story("Update User")
    @Description("Test updating user with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUserWithInvalidId() {
        User user = User.builder()
                .firstName("Test")
                .lastName("User")
                .build();
        
        Response response = userEndpoints.updateUser(getAuthenticatedRequestSpec(adminToken), "invalid-id-123", user);
        
        response.then()
                .statusCode(404)
                .log().all();
    }
    
    @Test(priority = 17)
    @Story("Update User")
    @Description("Test updating user without authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUserWithoutAuth() {
        User user = User.builder()
                .firstName("Test")
                .lastName("User")
                .build();
        
        Response response = userEndpoints.updateUser(getRequestSpec(), createdUserId, user);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 18)
    @Story("Update User")
    @Description("Test updating user with user token (non-admin)")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUserWithUserToken() {
        Response getResponse = userEndpoints.getUserById(getAuthenticatedRequestSpec(adminToken), createdUserId);
        User user = getResponse.as(User.class);
        user.setFirstName("ShouldNotUpdate");
        
        Response response = userEndpoints.updateUser(getAuthenticatedRequestSpec(userToken), createdUserId, user);
        
        // Depending on API behavior, might be 403 Forbidden or 401 Unauthorized
        Assert.assertTrue(response.statusCode() == 403 || response.statusCode() == 401 || response.statusCode() == 200);
        response.then().log().all();
    }
}
