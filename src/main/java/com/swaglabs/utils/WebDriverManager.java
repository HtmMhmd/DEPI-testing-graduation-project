package com.swaglabs.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import java.time.Duration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.Arrays;  // Add import for Arrays

/**
 * WebDriver utility class for managing browser instances
 * Provides features like headless mode, screenshot capture, and retry logic
 * Supports multiple browsers: Chrome, Firefox, Edge, and Safari
 */
public class WebDriverManager {
    private static WebDriver driver;
    private static final String BASE_URL = "https://www.saucedemo.com";
    private static final int DEFAULT_TIMEOUT = 10; // seconds
    private static final int DEFAULT_RETRY_COUNT = 3;
    private static final String SCREENSHOTS_DIR = "test-screenshots";
    
    // Environment flags
    private static final boolean HEADLESS_MODE = Boolean.parseBoolean(
            System.getProperty("headless", "false"));
    private static final boolean CI_MODE = Boolean.parseBoolean(
            System.getProperty("ci", "false"));
            
    // Browser selection - defaults to chrome if not specified
    public enum BrowserType {
        CHROME, FIREFOX, EDGE, SAFARI
    }
      private static final BrowserType BROWSER_TYPE = getBrowserTypeFromProperty();
    
    /**
     * Get the browser type from system property
     */
    private static BrowserType getBrowserTypeFromProperty() {
        String browserProperty = System.getProperty("browser", "chrome").toLowerCase();
        try {
            switch (browserProperty) {
                case "firefox":
                    return BrowserType.FIREFOX;
                case "edge":
                    return BrowserType.EDGE;
                case "safari":
                    return BrowserType.SAFARI;
                case "chrome":
                default:
                    return BrowserType.CHROME;
            }
        } catch (Exception e) {
            System.err.println("Invalid browser specified, defaulting to Chrome: " + e.getMessage());
            return BrowserType.CHROME;
        }
    }

    /**
     * Get the driver instance, creating it if needed
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            setupDriver();
        }
        return driver;
    }    /**
     * Setup a new WebDriver instance with appropriate configurations
     */
    public static void setupDriver() {
        try {
            System.out.println("Setting up " + BROWSER_TYPE + " browser...");
            
            switch (BROWSER_TYPE) {
                case FIREFOX:
                    setupFirefoxDriver();
                    break;
                case EDGE:
                    setupEdgeDriver();
                    break;
                case SAFARI:
                    setupSafariDriver();
                    break;
                case CHROME:
                default:
                    setupChromeDriver();
                    break;
            }
            
            // Configure timeouts for all browser types
            if (driver != null) {
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                System.out.println(BROWSER_TYPE + " WebDriver successfully initialized");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize WebDriver: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Set up Chrome WebDriver
     */
    private static void setupChromeDriver() {
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Enable headless mode if running in CI or if explicitly requested
        if (HEADLESS_MODE || CI_MODE) {
            options.addArguments("--headless=new");
            System.out.println("Running in headless mode");
        }
        
        // Common Chrome options
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-extensions");
        
        // Add options to handle network issues
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        // Additional options for stability
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.setExperimentalOption("excludeSwitches", 
                java.util.Collections.singletonList("enable-automation"));
        
        driver = new ChromeDriver(options);
    }
    
    /**
     * Set up Firefox WebDriver
     */
    private static void setupFirefoxDriver() {
        io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (HEADLESS_MODE || CI_MODE) {
            options.addArguments("-headless");
            System.out.println("Running in headless mode");
        }
        
        // Common Firefox options
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        driver = new FirefoxDriver(options);
    }
    
    /**
     * Set up Edge WebDriver
     */
    private static void setupEdgeDriver() {
        io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (HEADLESS_MODE || CI_MODE) {
            options.addArguments("--headless=new");
            System.out.println("Running in headless mode");
        }
        
        // Common Edge options
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-extensions");
        
        driver = new EdgeDriver(options);
    }
    
    /**
     * Set up Safari WebDriver
     */
    private static void setupSafariDriver() {
        // Safari doesn't support WebDriverManager, as WebDriver is built into macOS
        // Safari doesn't support headless mode
        if (HEADLESS_MODE || CI_MODE) {
            System.out.println("Warning: Safari does not support headless mode. Continuing with normal mode.");
        }
        
        SafariOptions options = new SafariOptions();
        // Safari has limited options compared to other browsers
        
        driver = new SafariDriver(options);
    }
      /**
     * Create a new WebDriver instance that's independent of the singleton instance
     * Honors the browser type selected via system property
     */
    public static WebDriver createNewDriver() {
        return createNewDriver(BROWSER_TYPE);
    }
    
    /**
     * Create a new WebDriver instance with the specified browser type
     * @param browserType The type of browser to create
     * @return A new WebDriver instance
     */
    public static WebDriver createNewDriver(BrowserType browserType) {
        WebDriver newDriver = null;
        
        try {
            switch (browserType) {
                case FIREFOX:
                    io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (HEADLESS_MODE || CI_MODE) {
                        firefoxOptions.addArguments("-headless");
                    }
                    newDriver = new FirefoxDriver(firefoxOptions);
                    break;
                    
                case EDGE:
                    io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (HEADLESS_MODE || CI_MODE) {
                        edgeOptions.addArguments("--headless=new");
                    }
                    edgeOptions.addArguments("--start-maximized");
                    newDriver = new EdgeDriver(edgeOptions);
                    break;
                    
                case SAFARI:
                    // Safari doesn't support WebDriverManager
                    newDriver = new SafariDriver(new SafariOptions());
                    break;
                    
                case CHROME:
                default:
                    io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (HEADLESS_MODE || CI_MODE) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--ignore-certificate-errors");
                    chromeOptions.addArguments("--disable-web-security");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--dns-prefetch-disable");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    newDriver = new ChromeDriver(chromeOptions);
                    break;
            }
            
            if (newDriver != null) {
                newDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
                newDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            }
            
        } catch (Exception e) {
            System.err.println("Failed to create new driver: " + e.getMessage());
            throw e;
        }
        
        return newDriver;
    }

    public static void navigateToBaseUrl() {
        navigateToBaseUrlWithRetry(DEFAULT_RETRY_COUNT);
    }
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
     * Quit the WebDriver and release resources
     */
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("WebDriver successfully closed");
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
    
    /**
     * Capture a screenshot with the current WebDriver
     * @param testName Name of test for the screenshot filename
     * @return File object of the saved screenshot, or null if failed
     */
    public static File captureScreenshot(String testName) {
        if (driver == null) {
            System.err.println("Cannot capture screenshot: WebDriver is null");
            return null;
        }
        
        try {
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            // Create screenshots directory if it doesn't exist
            Path screenshotsDir = Paths.get(SCREENSHOTS_DIR);
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }
            
            // Generate filename with timestamp
            String fileName = sanitizeFileName(testName) + "-" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".png";
            Path targetPath = screenshotsDir.resolve(fileName);
            
            // Save the screenshot
            Files.copy(screenshot.toPath(), targetPath);
            System.out.println("Screenshot saved: " + targetPath);
            
            return targetPath.toFile();
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error capturing screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Sanitize a filename by removing invalid characters
     */
    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9-]", "_");
    }
    
    /**
     * Check if the browser is reachable and working properly
     * @return true if browser is working correctly
     */
    public static boolean testBrowserConnection() {
        try {
            WebDriver testDriver = createNewDriver();
            try {
                testDriver.get("https://www.google.com");
                System.out.println("Browser connection test: SUCCESS");
                return true;
            } finally {
                testDriver.quit();
            }
        } catch (Exception e) {
            System.err.println("Browser connection test FAILED: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if the Swag Labs site is reachable
     * @return true if the site is reachable
     */
    public static boolean isSwagLabsReachable() {
        try {
            WebDriver testDriver = createNewDriver();
            try {
                testDriver.get(BASE_URL);
                // Check if login form is present
                boolean reachable = !testDriver.findElements(By.id("user-name")).isEmpty();
                System.out.println("Swag Labs reachability check: " + (reachable ? "SUCCESS" : "FAILED"));
                return reachable;
            } finally {
                testDriver.quit();
            }
        } catch (Exception e) {
            System.err.println("Swag Labs reachability check FAILED: " + e.getMessage());
            return false;
        }
    }
}