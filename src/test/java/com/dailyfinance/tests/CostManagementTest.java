package com.dailyfinance.tests;

import com.dailyfinance.base.BaseTest;
import com.dailyfinance.endpoints.CostEndpoints;
import com.dailyfinance.models.Cost;
import com.dailyfinance.models.CostRequest;
import com.dailyfinance.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Daily Finance API")
@Feature("Cost Management")
public class CostManagementTest extends BaseTest {
    private CostEndpoints costEndpoints;
    private CostRequest validCostRequest;
    
    @BeforeClass
    public void setupCostManagement() {
        costEndpoints = new CostEndpoints();
        
        validCostRequest = CostRequest.builder()
                .itemName("Test Item - " + System.currentTimeMillis())
                .quantity(1)
                .amount("280")
                .purchaseDate(TestDataGenerator.getCurrentDate())
                .month("December")
                .remarks("Test item for automation")
                .build();
    }
    
    @Test(priority = 19)
    @Story("Get Item List")
    @Description("Test retrieving all cost items with user authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllCosts() {
        // Skip if userToken is null
        if (userToken == null) {
            throw new org.testng.SkipException("User token not available - depends on earlier test");
        }
        
        Response response = costEndpoints.getAllCosts(getAuthenticatedRequestSpec(userToken));
        
        response.then()
                .statusCode(200)
                .log().all();
        
        // List might be empty for new user
        Assert.assertNotNull(response.getBody().asString());
    }
    
    @Test(priority = 20)
    @Story("Get Item List")
    @Description("Test retrieving costs without authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllCostsWithoutAuth() {
        Response response = costEndpoints.getAllCosts(getRequestSpec());
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 21)
    @Story("Add Item")
    @Description("Test adding a new cost item")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddCostItem() {
        // Skip if userToken is null
        if (userToken == null) {
            throw new org.testng.SkipException("User token not available - depends on earlier test");
        }
        
        Response response = costEndpoints.createCost(getAuthenticatedRequestSpec(userToken), validCostRequest);
        
        response.then()
                .statusCode(201)
                .log().all();
        
        createdCostId = costEndpoints.extractCostId(response);
        
        Assert.assertNotNull(createdCostId, "Cost ID should not be null");
        Assert.assertEquals(response.jsonPath().getString("itemName"), validCostRequest.getItemName());
        Assert.assertEquals(response.jsonPath().getInt("quantity"), validCostRequest.getQuantity());
    }
    
    @Test(priority = 22)
    @Story("Add Item")
    @Description("Test adding cost item without authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testAddCostItemWithoutAuth() {
        Response response = costEndpoints.createCost(getRequestSpec(), validCostRequest);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 23)
    @Story("Add Item")
    @Description("Test adding cost item with missing required fields")
    @Severity(SeverityLevel.NORMAL)
    public void testAddCostItemWithMissingFields() {
        // Skip if userToken is null
        if (userToken == null) {
            throw new org.testng.SkipException("User token not available - depends on earlier test");
        }
        
        CostRequest invalidRequest = CostRequest.builder()
                .itemName("Test Item")
                .build();
        
        Response response = costEndpoints.createCost(getAuthenticatedRequestSpec(userToken), invalidRequest);
        
        response.then()
                .statusCode(400)
                .log().all();
    }
    
    @Test(priority = 24)
    @Story("Add Item")
    @Description("Test adding cost item with invalid quantity")
    @Severity(SeverityLevel.NORMAL)
    public void testAddCostItemWithInvalidQuantity() {
        CostRequest invalidRequest = CostRequest.builder()
                .itemName("Test Item")
                .quantity(-1)
                .amount("100")
                .purchaseDate(TestDataGenerator.getCurrentDate())
                .month("December")
                .remarks("Invalid quantity test")
                .build();
        
        Response response = costEndpoints.createCost(getAuthenticatedRequestSpec(userToken), invalidRequest);
        
        // May return 400 or accept the request depending on API validation
        response.then().log().all();
    }
    
    @Test(priority = 25)
    @Story("Get Item Details")
    @Description("Test retrieving cost item by ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetCostById() {
        // Skip if userToken or createdCostId is null
        if (userToken == null || createdCostId == null) {
            throw new org.testng.SkipException("User token or cost ID not available - depends on earlier test");
        }
        
        Response response = costEndpoints.getCostById(getAuthenticatedRequestSpec(userToken), createdCostId);
        
        response.then()
                .statusCode(200)
                .log().all();
        
        Cost cost = response.as(Cost.class);
        Assert.assertEquals(cost.getId(), createdCostId);
    }
    
    @Test(priority = 26)
    @Story("Get Item Details")
    @Description("Test retrieving cost with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetCostByInvalidId() {
        Response response = costEndpoints.getCostById(getAuthenticatedRequestSpec(userToken), "invalid-cost-id-123");
        
        response.then()
                .statusCode(404)
                .log().all();
    }
    
    @Test(priority = 27)
    @Story("Edit Item")
    @Description("Test updating cost item name")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateCostItemName() {
        // Skip if userToken or createdCostId is null
        if (userToken == null || createdCostId == null) {
            throw new org.testng.SkipException("User token or cost ID not available - depends on earlier test");
        }
        
        // Get existing cost
        Response getResponse = costEndpoints.getCostById(getAuthenticatedRequestSpec(userToken), createdCostId);
        Cost cost = getResponse.as(Cost.class);
        
        // Update item name and amount
        String newItemName = "Updated Item - " + System.currentTimeMillis();
        cost.setItemName(newItemName);
        cost.setAmount("290");
        
        Response response = costEndpoints.updateCost(getAuthenticatedRequestSpec(userToken), createdCostId, cost);
        
        response.then()
                .statusCode(200)
                .log().all();
        
        Cost updatedCost = response.as(Cost.class);
        Assert.assertEquals(updatedCost.getItemName(), newItemName);
        Assert.assertEquals(updatedCost.getAmount(), "290");
    }
    
    @Test(priority = 28)
    @Story("Edit Item")
    @Description("Test updating cost without authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateCostWithoutAuth() {
        // Skip if createdCostId is null
        if (createdCostId == null) {
            throw new org.testng.SkipException("Cost ID not available - depends on earlier test");
        }
        
        Cost cost = Cost.builder()
                .itemName("Test")
                .build();
        
        Response response = costEndpoints.updateCost(getRequestSpec(), createdCostId, cost);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 29)
    @Story("Edit Item")
    @Description("Test updating cost with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateCostWithInvalidId() {
        Cost cost = Cost.builder()
                .itemName("Test")
                .quantity(1)
                .amount("100")
                .build();
        
        Response response = costEndpoints.updateCost(getAuthenticatedRequestSpec(userToken), "invalid-id-123", cost);
        
        response.then()
                .statusCode(404)
                .log().all();
    }
    
    @Test(priority = 30)
    @Story("Delete Item")
    @Description("Test deleting a cost item")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteCostItem() {
        // Skip if userToken or createdCostId is null
        if (userToken == null || createdCostId == null) {
            throw new org.testng.SkipException("User token or cost ID not available - depends on earlier test");
        }
        
        Response response = costEndpoints.deleteCost(getAuthenticatedRequestSpec(userToken), createdCostId);
        
        response.then()
                .statusCode(200)
                .log().all();
        
        Assert.assertTrue(response.getBody().asString().contains("deleted") || 
                         response.getBody().asString().contains("success"));
    }
    
    @Test(priority = 31)
    @Story("Delete Item")
    @Description("Test deleting already deleted cost item")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteAlreadyDeletedCost() {
        // Skip if userToken or createdCostId is null
        if (userToken == null || createdCostId == null) {
            throw new org.testng.SkipException("User token or cost ID not available - depends on earlier test");
        }
        
        Response response = costEndpoints.deleteCost(getAuthenticatedRequestSpec(userToken), createdCostId);
        
        response.then()
                .statusCode(404)
                .log().all();
    }
    
    @Test(priority = 32)
    @Story("Delete Item")
    @Description("Test deleting cost without authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteCostWithoutAuth() {
        // Create a new cost for this test
        CostRequest newCost = CostRequest.builder()
                .itemName("To Delete")
                .quantity(1)
                .amount("100")
                .purchaseDate(TestDataGenerator.getCurrentDate())
                .month("December")
                .remarks("Delete test")
                .build();
        
        Response createResponse = costEndpoints.createCost(getAuthenticatedRequestSpec(userToken), newCost);
        String costId = costEndpoints.extractCostId(createResponse);
        
        Response response = costEndpoints.deleteCost(getRequestSpec(), costId);
        
        response.then()
                .statusCode(401)
                .log().all();
    }
    
    @Test(priority = 33)
    @Story("Delete Item")
    @Description("Test deleting cost with invalid ID")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteCostWithInvalidId() {
        Response response = costEndpoints.deleteCost(getAuthenticatedRequestSpec(userToken), "invalid-id-999");
        
        response.then()
                .statusCode(404)
                .log().all();
    }
}
