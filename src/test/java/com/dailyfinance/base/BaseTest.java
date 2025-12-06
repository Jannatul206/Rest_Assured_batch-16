package com.dailyfinance.base;

import com.dailyfinance.utils.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected static String adminToken;
    protected static String userToken;
    protected static String createdUserId;
    protected static String createdCostId;
    
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.getBaseUri();
        RestAssured.basePath = ConfigReader.getBasePath();
    }
    
    protected RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }
    
    protected RequestSpecification getAuthenticatedRequestSpec(String token) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }
}
