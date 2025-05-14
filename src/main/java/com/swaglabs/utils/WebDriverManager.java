package com.swaglabs.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class WebDriverManager {
    private static WebDriver driver;
    private static final String BASE_URL = "https://www.saucedemo.com";

    public static WebDriver getDriver() {
        if (driver == null) {
            setupDriver();
        }
        return driver;
    }

    public static void setupDriver() {
        // Setup Chrome WebDriver
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    // Create a new WebDriver instance that's independent of the singleton instance
    public static WebDriver createNewDriver() {
        // Setup Chrome WebDriver
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        
        WebDriver newDriver = new ChromeDriver(options);
        newDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return newDriver;
    }

    public static void navigateToBaseUrl() {
        getDriver().get(BASE_URL);
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}