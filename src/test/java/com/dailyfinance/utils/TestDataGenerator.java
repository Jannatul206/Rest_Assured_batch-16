package com.dailyfinance.utils;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TestDataGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    public static String generateEmail() {
        return faker.internet().emailAddress();
    }
    
    public static String generateFirstName() {
        return faker.name().firstName();
    }
    
    public static String generateLastName() {
        return faker.name().lastName();
    }
    
    public static String generatePhoneNumber() {
        return "01" + (random.nextInt(900000000) + 100000000);
    }
    
    public static String generateAddress() {
        return faker.address().fullAddress();
    }
    
    public static String generatePassword() {
        return faker.internet().password(8, 16);
    }
    
    public static String generateItemName() {
        return faker.commerce().productName();
    }
    
    public static String generateAmount() {
        return String.valueOf(random.nextInt(10000) + 100);
    }
    
    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    public static String getCurrentMonth() {
        return LocalDate.now().getMonth().name();
    }
}
