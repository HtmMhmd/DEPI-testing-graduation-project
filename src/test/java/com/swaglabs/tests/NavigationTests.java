package com.swaglabs.tests;

import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Swag Labs navigation and UI functionality
 * Authors: Omar Shahin, Hatem Mohamed
 */
public class NavigationTests {
    
    private WebDriver driver;
    private ProductsPage productsPage;
    private WebDriverWait wait;
    
    @BeforeEach
    public void setup() {
        try {
            WebDriverManager.setupDriver();
            WebDriverManager.navigateToBaseUrl();
            driver = WebDriverManager.getDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Login and navigate to products page
            LoginPage loginPage = new LoginPage();
            productsPage = loginPage.loginAs(LoginPage.STANDARD_USER, LoginPage.PASSWORD);
        } catch (Exception e) {
            System.out.println("Exception in setup: " + e.getMessage());
            if (driver != null) {
                try {
                    WebDriverManager.quitDriver();
                } catch (Exception quitEx) {
                    // Ignore
                }
            }
            fail("Setup failed: " + e.getMessage());
        }
    }
    
    @AfterEach
    public void tearDown() {
        // Only quit the driver if it's not already quit
        try {
            if (driver != null && driver.getWindowHandles().size() > 0) {
                WebDriverManager.quitDriver();
            }
        } catch (Exception e) {
            // Driver is already quit or has other issues, just log it
            System.out.println("Exception in tearDown: " + e.getMessage());
        }
    }
    
    /**
     * TC-023: Hamburger Menu
     * Steps:
     * 1. Click hamburger menu icon
     * Expected Result: Menu options displayed
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-023: Hamburger Menu")
    public void testHamburgerMenu() {
        // Click hamburger menu button with retry
        int retryCount = 0;
        int maxRetries = 3;
        boolean menuOpened = false;
        
        while (!menuOpened && retryCount < maxRetries) {
            try {
                // Click menu button
                productsPage.openMenu();
                
                // Add extra time for menu animation
                Thread.sleep(1000);
                
                // Explicitly wait for menu to appear
                productsPage.waitForMenuToAppear();
                
                // Verify menu is displayed
                menuOpened = true;
            } catch (Exception e) {
                System.out.println("Attempt " + (retryCount + 1) + " to open menu failed: " + e.getMessage());
                retryCount++;
            }
        }
        
        if (!menuOpened) {
            fail("Failed to open menu after " + maxRetries + " attempts");
        }
        
        // Now check for menu items
        try {
            // Wait for menu items to be visible
            WebElement menuContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu")));
            
            // Get menu text for easier assertions
            String menuText = menuContainer.getText().toLowerCase();
            
            // Verify expected menu options
            assertTrue(menuText.contains("logout"), "Menu should contain Logout option");
            assertTrue(menuText.contains("about"), "Menu should contain About option");
            assertTrue(menuText.contains("reset"), "Menu should contain Reset option");
            
        } catch (Exception e) {
            fail("Failed to verify menu items: " + e.getMessage());
        }
    }
    
    /**
     * TC-024: About Page
     * Steps:
     * 1. Open hamburger menu
     * 2. Click "About"
     * Expected Result: About page displayed
     * Priority: Low
     */
    @Test
    @DisplayName("TC-024: About Page")
    public void testAboutPage() {
        // Open the hamburger menu
        productsPage.openMenu();
        productsPage.waitForMenuToAppear();
        
        // Wait for About link to be clickable before clicking
        WebElement aboutLink = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("about_sidebar_link")));
        aboutLink.click();
        
        // Verify user is redirected to Sauce Labs website
        wait.until(ExpectedConditions.urlContains("saucelabs"));
        assertTrue(driver.getCurrentUrl().contains("saucelabs.com"),
                "User should be redirected to Sauce Labs website");
    }
    
    /**
     * TC-025: Responsive Design - Mobile
     * Steps:
     * 1. Access website on mobile device or using responsive design mode
     * Expected Result: Website adapts properly to small screen
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-025: Responsive Design - Mobile")
    public void testResponsiveDesignMobile() {
        // Set viewport to mobile size (iPhone X dimensions)
        driver.manage().window().setSize(new Dimension(375, 812));
        
        // Refresh page to ensure responsive layout loads
        driver.navigate().refresh();
        
        // Wait for elements to be visible after refresh
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-burger-menu-btn")));
        
        // Verify the menu button is displayed in mobile view
        assertTrue(driver.findElement(By.id("react-burger-menu-btn")).isDisplayed(),
                "Hamburger menu button should be displayed in mobile view");
        
        // Verify products are still visible
        assertTrue(driver.findElement(By.className("inventory_list")).isDisplayed(),
                "Product list should be visible in mobile view");
    }
    
    /**
     * TC-026: Responsive Design - Tablet
     * Steps:
     * 1. Access website on tablet device or using responsive design mode
     * Expected Result: Website adapts properly to medium screen
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-026: Responsive Design - Tablet")
    public void testResponsiveDesignTablet() {
        // Set viewport to tablet size (iPad dimensions)
        driver.manage().window().setSize(new Dimension(768, 1024));
        
        // Refresh page to ensure responsive layout loads
        driver.navigate().refresh();
        
        // Wait for elements to be visible after refresh
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-burger-menu-btn")));
        
        // Verify the menu button is displayed in tablet view
        assertTrue(driver.findElement(By.id("react-burger-menu-btn")).isDisplayed(),
                "Hamburger menu button should be displayed in tablet view");
        
        // Verify products are visible in tablet view
        assertTrue(driver.findElement(By.className("inventory_list")).isDisplayed(),
                "Product list should be visible in tablet view");
    }
    
    /**
     * TC-028: Invalid URL Access
     * Steps:
     * 1. Attempt to access internal pages without logging in
     * Expected Result: User redirected to login page
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-028: Invalid URL Access")
    public void testInvalidUrlAccess() {
        // We'll create a separate WebDriver session for this test
        // to avoid issues with the main session
        WebDriver testDriver = null;
        WebDriverWait testWait = null;
        
        try {
            // Setup a new driver instance
            testDriver = WebDriverManager.createNewDriver();
            testWait = new WebDriverWait(testDriver, Duration.ofSeconds(10));
            
            // Try to access inventory page directly without logging in
            testDriver.get("https://www.saucedemo.com/inventory.html");
            
            // Wait for redirection and login button to be visible
            testWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
            
            // Verify user is redirected to login page
            assertTrue(testDriver.getCurrentUrl().contains("saucedemo.com"),
                    "User should be redirected to login page");
            assertTrue(testDriver.findElement(By.id("login-button")).isDisplayed(),
                    "Login button should be visible when trying to access protected pages without login");
        } catch (Exception e) {
            System.out.println("Exception in testInvalidUrlAccess: " + e.getMessage());
            if (testDriver != null) {
                fail("Error testing invalid URL access: " + e.getMessage());
            }
        } finally {
            // Clean up the separate driver instance
            if (testDriver != null) {
                try {
                    testDriver.quit();
                } catch (Exception e) {
                    System.out.println("Exception quitting test driver: " + e.getMessage());
                }
            }
        }
    }
}
