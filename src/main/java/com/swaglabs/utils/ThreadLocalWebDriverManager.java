package com.swaglabs.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.time.Duration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

/**
 * WebDriver manager using ThreadLocal for parallel test execution
 */
public class ThreadLocalWebDriverManager {
    // ThreadLocal for parallel execution
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final String BASE_URL = "https://www.saucedemo.com";
    private static final int DEFAULT_TIMEOUT = 10; // seconds
    private static final int DEFAULT_RETRY_COUNT = 3;
    private static final String SCREENSHOTS_DIR = "test-screenshots";

    /**
     * Get the WebDriver instance for the current thread
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            setupDriver();
        }
        return driverThreadLocal.get();
    }

    /**
     * Setup a new WebDriver instance for the current thread
     */    public static void setupDriver() {
        // Setup Chrome WebDriver
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-extensions");
        
        // Add options to handle network issues
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); // Longer timeout
        
        // Store in ThreadLocal
        driverThreadLocal.set(driver);
    }
    
    /**
     * Create a new WebDriver instance independent of ThreadLocal
     */
    public static WebDriver createNewDriver() {
        // Setup Chrome WebDriver
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-extensions");
        
        // Add options to handle network issues
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        WebDriver newDriver = new ChromeDriver(options);
        newDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
        newDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return newDriver;
    }

    /**
     * Navigate to the base URL
     */
    public static void navigateToBaseUrl() {
        navigateToBaseUrlWithRetry(DEFAULT_RETRY_COUNT);
    }
    
    /**
     * Navigate to the base URL with retry mechanism
     */
    public static void navigateToBaseUrlWithRetry(int maxRetries) {
        int retryCount = 0;
        boolean navigated = false;
        
        while (!navigated && retryCount < maxRetries) {
            try {
                getDriver().get(BASE_URL);
                navigated = true;
            } catch (Exception e) {
                retryCount++;
                System.out.println("Navigation attempt " + retryCount + " failed: " + e.getMessage());
                
                if (retryCount >= maxRetries) {
                    System.out.println("Maximum retry attempts reached. Navigation failed.");
                    throw e;
                }
                
                try {
                    // Wait before retrying
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Quit the WebDriver instance for the current thread
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Error quitting driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Capture screenshot with the current WebDriver
     */
    public static File captureScreenshot(String testName) {
        try {
            WebDriver driver = getDriver();
            if (driver != null) {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                
                // Create screenshots directory if it doesn't exist
                Path screenshotsDir = Paths.get(SCREENSHOTS_DIR);
                if (!Files.exists(screenshotsDir)) {
                    Files.createDirectories(screenshotsDir);
                }
                
                // Generate filename and save
                String fileName = sanitizeFileName(testName) + "-" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".png";
                Path targetPath = screenshotsDir.resolve(fileName);
                Files.copy(screenshot.toPath(), targetPath);
                
                return targetPath.toFile();
            }
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Sanitize filename for screenshot
     */
    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9-]", "_");
    }
}
