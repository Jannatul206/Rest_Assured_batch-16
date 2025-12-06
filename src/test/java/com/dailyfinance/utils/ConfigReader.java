package com.dailyfinance.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    
    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getBaseUri() {
        return getProperty("base.uri");
    }
    
    public static String getBasePath() {
        return getProperty("base.path");
    }
    
    public static String getAdminEmail() {
        return getProperty("admin.email");
    }
    
    public static String getAdminPassword() {
        return getProperty("admin.password");
    }
}
