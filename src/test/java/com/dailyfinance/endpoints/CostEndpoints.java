package com.dailyfinance.endpoints;

import com.dailyfinance.models.Cost;
import com.dailyfinance.models.CostRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CostEndpoints {
    private static final String COSTS_ENDPOINT = "/costs";
    private static final String COST_BY_ID_ENDPOINT = "/costs/{costId}";
    
    @Step("Get all costs")
    public Response getAllCosts(RequestSpecification requestSpec) {
        return given()
                .spec(requestSpec)
                .when()
                .get(COSTS_ENDPOINT);
    }
    
    @Step("Get cost by ID: {costId}")
    public Response getCostById(RequestSpecification requestSpec, String costId) {
        return given()
                .spec(requestSpec)
                .pathParam("costId", costId)
                .when()
                .get(COST_BY_ID_ENDPOINT);
    }
    
    @Step("Create new cost item: {request.itemName}")
    public Response createCost(RequestSpecification requestSpec, CostRequest request) {
        return given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post(COSTS_ENDPOINT);
    }
    
    @Step("Update cost with ID: {costId}")
    public Response updateCost(RequestSpecification requestSpec, String costId, Cost cost) {
        return given()
                .spec(requestSpec)
                .pathParam("costId", costId)
                .body(cost)
                .when()
                .put(COST_BY_ID_ENDPOINT);
    }
    
    @Step("Delete cost with ID: {costId}")
    public Response deleteCost(RequestSpecification requestSpec, String costId) {
        return given()
                .spec(requestSpec)
                .pathParam("costId", costId)
                .when()
                .delete(COST_BY_ID_ENDPOINT);
    }
    
    @Step("Extract cost ID from response")
    public String extractCostId(Response response) {
        Cost cost = response.as(Cost.class);
        return cost.getId();
    }
}
