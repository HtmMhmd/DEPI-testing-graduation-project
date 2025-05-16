package com.swaglabs.tests;

import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.CheckoutPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.WebDriverManager;
import com.swaglabs.utils.TestLogger;
import com.swaglabs.enums.UserType;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for different user types in Swag Labs
 * Tests behavior of standard_user, locked_out_user, problem_user, etc.
 */
public class UserTypeTests {
    
    private WebDriver driver;
    private LoginPage loginPage;
    private static final String TEST_PRODUCT = "Sauce Labs Backpack";
    private WebDriverWait wait;
    
    @BeforeAll
    public static void setupClass() {
        // Create log directories if they don't exist
        System.out.println("Setting up test class and logging directories");
    }
    
    @BeforeEach
    public void setup() {
        WebDriverManager.setupDriver();
        WebDriverManager.navigateToBaseUrl();
        driver = WebDriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Longer timeout for performance_glitch_user
        loginPage = new LoginPage();
    }
    
    @AfterEach
    public void tearDown() {
        WebDriverManager.quitDriver();
    }
    
    @AfterAll
    public static void tearDownClass() {
        // Generate test summary report
        TestLogger.generateTestSummary();
        System.out.println("Test summary report generated in logs/test-summary.md");
    }
    
    /**
     * TC-101: Standard User Login
     * Tests that standard_user can login and access all features normally
     */
    @Test
    @DisplayName("TC-101: Standard User Login")
    public void testStandardUser() {
        UserType userType = UserType.STANDARD_USER;
        TestLogger.logTestStart(userType, "Standard User Login Flow");
        
        try {
            // Login as standard user
            TestLogger.logTestStep(userType, "Logging in as standard user");
            ProductsPage productsPage = loginPage.loginAs(userType.getUsername(), LoginPage.PASSWORD);
            
            // Verify user is on products page
            TestLogger.logTestStep(userType, "Verifying redirect to products page");
            boolean onProductsPage = productsPage.isOnProductsPage();
            TestLogger.logInfo(userType, "On products page: " + onProductsPage);
            assertTrue(onProductsPage, "Standard user should be able to login successfully");
            
            // Add product to cart
            TestLogger.logTestStep(userType, "Adding product to cart: " + TEST_PRODUCT);
            productsPage.clickAddToCartForProduct(TEST_PRODUCT);
            int cartCount = productsPage.getCartCount();
            TestLogger.logInfo(userType, "Cart count after adding product: " + cartCount);
            assertEquals(1, cartCount, "Standard user should be able to add product to cart");
            
            // Complete a simple checkout process
            TestLogger.logTestStep(userType, "Navigating to cart page");
            CartPage cartPage = productsPage.goToCart();
            TestLogger.logTestStep(userType, "Starting checkout process");
            CheckoutPage checkoutPage = cartPage.checkout();
            
            // Fill checkout info and verify checkout steps work
            TestLogger.logTestStep(userType, "Filling customer information");
            checkoutPage.fillCustomerInfo("Standard", "User", "12345");
            TestLogger.logTestStep(userType, "Continuing to checkout step two");
            checkoutPage.clickContinue();
            
            // Verify on checkout step two
            TestLogger.logTestStep(userType, "Verifying user is on checkout step two");
            boolean onCheckoutStepTwo = checkoutPage.isOnCheckoutStepTwo();
            TestLogger.logInfo(userType, "On checkout step two: " + onCheckoutStepTwo);
            assertTrue(onCheckoutStepTwo, "Standard user should be able to complete checkout");
            
            TestLogger.logTestEnd(userType, "Standard User Login Flow", true, "All steps completed successfully");
        } catch (Exception e) {
            TestLogger.logError(userType, "Test failed with exception", e);
            TestLogger.logTestEnd(userType, "Standard User Login Flow", false, "Test failed: " + e.getMessage());
            fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC-102: Locked Out User Login
     * Tests that locked_out_user cannot login and sees appropriate error
     */
    @Test
    @DisplayName("TC-102: Locked Out User Login")
    public void testLockedOutUser() {
        UserType userType = UserType.LOCKED_OUT_USER;
        TestLogger.logTestStart(userType, "Locked Out User Login Test");
        
        try {
            // Try to login as locked out user
            TestLogger.logTestStep(userType, "Attempting to login as locked out user");
            loginPage.loginAs(userType.getUsername(), LoginPage.PASSWORD);
            
            // Verify error message is displayed
            TestLogger.logTestStep(userType, "Verifying error message is displayed");
            boolean errorDisplayed = loginPage.isLoginErrorDisplayed();
            TestLogger.logInfo(userType, "Error message displayed: " + errorDisplayed);
            assertTrue(errorDisplayed, "Locked out user should see an error message");
            
            // Verify error message contains "locked out" text
            TestLogger.logTestStep(userType, "Verifying error message contains 'locked out' text");
            String errorMessage = loginPage.getErrorMessage().toLowerCase();
            TestLogger.logInfo(userType, "Error message: " + errorMessage);
            boolean containsLockedOut = errorMessage.contains("locked out");
            TestLogger.logInfo(userType, "Error message contains 'locked out': " + containsLockedOut);
            assertTrue(containsLockedOut, "Error should indicate that user is locked out");
            
            TestLogger.logTestEnd(userType, "Locked Out User Login Test", true, "All steps completed successfully");
        } catch (Exception e) {
            TestLogger.logError(userType, "Test failed with exception", e);
            TestLogger.logTestEnd(userType, "Locked Out User Login Test", false, "Test failed: " + e.getMessage());
            fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC-103: Problem User Login
     * Tests that problem_user can login but has UI/image issues
     */
    @Test
    @DisplayName("TC-103: Problem User Login")
    public void testProblemUser() {
        UserType userType = UserType.PROBLEM_USER;
        TestLogger.logTestStart(userType, "Problem User Login & UI Issues Test");
        
        try {
            // Login as problem user
            TestLogger.logTestStep(userType, "Logging in as problem user");
            ProductsPage productsPage = loginPage.loginAs(userType.getUsername(), LoginPage.PASSWORD);
            
            // Verify user is on products page
            TestLogger.logTestStep(userType, "Verifying redirect to products page");
            boolean onProductsPage = productsPage.isOnProductsPage();
            TestLogger.logInfo(userType, "On products page: " + onProductsPage);
            assertTrue(onProductsPage, "Problem user should be able to login successfully");
            
            // Check for UI inconsistencies - all product images should be the same
            TestLogger.logTestStep(userType, "Checking for UI inconsistencies with product images");
            List<WebElement> productImages = driver.findElements(By.className("inventory_item_img"));
            TestLogger.logInfo(userType, "Number of product images found: " + productImages.size());
            
            if (productImages.size() > 0) {
                String firstImageSrc = productImages.get(0).findElement(By.tagName("img")).getAttribute("src");
                TestLogger.logInfo(userType, "First image source: " + firstImageSrc);
                
                // Check if all images have the same source (problem_user bug)
                boolean allImagesSame = true;
                for (WebElement imageContainer : productImages) {
                    WebElement image = imageContainer.findElement(By.tagName("img"));
                    String imageSrc = image.getAttribute("src");
                    TestLogger.logInfo(userType, "Image source: " + imageSrc);
                    if (!imageSrc.equals(firstImageSrc)) {
                        allImagesSame = false;
                        break;
                    }
                }
                
                TestLogger.logInfo(userType, "All product images are the same: " + allImagesSame);
                assertTrue(allImagesSame, "Problem user should see the same image for all products");
            }
            
            // Try sorting - another known issue for problem_user
            TestLogger.logTestStep(userType, "Testing sorting functionality (Z to A)");
            productsPage.sortProductsBy("Name (Z to A)");
            List<String> productNames = productsPage.getProductNames();
            TestLogger.logInfo(userType, "Product names after sorting: " + productNames);
            
            // For problem_user, sorting doesn't work properly
            boolean sortingWorking = productNames.get(0).compareTo(productNames.get(productNames.size() - 1)) > 0;
            TestLogger.logInfo(userType, "Sorting is working correctly: " + sortingWorking);
            assertFalse(sortingWorking, "Problem user's sorting functionality should not work properly");
            
            TestLogger.logTestEnd(userType, "Problem User Login & UI Issues Test", true, "All steps completed successfully");
        } catch (Exception e) {
            TestLogger.logError(userType, "Test failed with exception", e);
            TestLogger.logTestEnd(userType, "Problem User Login & UI Issues Test", false, "Test failed: " + e.getMessage());
            fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC-104: Performance Glitch User Login
     * Tests that performance_glitch_user experiences delay during login
     */
    @Test
    @DisplayName("TC-104: Performance Glitch User Login")
    public void testPerformanceGlitchUser() {
        UserType userType = UserType.PERFORMANCE_GLITCH_USER;
        TestLogger.logTestStart(userType, "Performance Glitch User Login Test");
        
        try {
            // Record start time
            TestLogger.logTestStep(userType, "Recording start time before login");
            long startTime = System.currentTimeMillis();
            TestLogger.logInfo(userType, "Start time: " + startTime);
            
            // Login as performance glitch user
            TestLogger.logTestStep(userType, "Logging in as performance glitch user");
            ProductsPage productsPage = loginPage.loginAs(userType.getUsername(), LoginPage.PASSWORD);
            
            // Record end time
            long endTime = System.currentTimeMillis();
            long loginTime = endTime - startTime;
            TestLogger.logInfo(userType, "End time: " + endTime);
            TestLogger.logInfo(userType, "Login duration: " + loginTime + " ms");
            
            // Verify user is on products page
            TestLogger.logTestStep(userType, "Verifying redirect to products page");
            boolean onProductsPage = productsPage.isOnProductsPage();
            TestLogger.logInfo(userType, "On products page: " + onProductsPage);
            assertTrue(onProductsPage, "Performance glitch user should be able to login successfully");
            
            // Verify login took longer than normal (typically > 2 seconds)
            // Note: This is a loose assertion as performance can vary
            TestLogger.logTestStep(userType, "Verifying login took longer than normal (> 2 seconds)");
            boolean isLoginSlow = loginTime > 2000;
            TestLogger.logInfo(userType, "Login took longer than 2 seconds: " + isLoginSlow);
            assertTrue(isLoginSlow, "Performance glitch user should experience a delay during login");
            
            TestLogger.logTestEnd(userType, "Performance Glitch User Login Test", true, 
                                "All steps completed successfully. Login time: " + loginTime + " ms");
        } catch (Exception e) {
            TestLogger.logError(userType, "Test failed with exception", e);
            TestLogger.logTestEnd(userType, "Performance Glitch User Login Test", false, "Test failed: " + e.getMessage());
            fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC-105: Error User Login
     * Tests that error_user can login but has cart functionality issues
     */
    @Test
    @DisplayName("TC-105: Error User Login")
    public void testErrorUser() {
        UserType userType = UserType.ERROR_USER;
        TestLogger.logTestStart(userType, "Error User Login & Cart Functionality Test");
        
        try {
            // Login as error user
            TestLogger.logTestStep(userType, "Logging in as error user");
            ProductsPage productsPage = loginPage.loginAs(userType.getUsername(), LoginPage.PASSWORD);
            
            // Verify user is on products page
            TestLogger.logTestStep(userType, "Verifying redirect to products page");
            boolean onProductsPage = productsPage.isOnProductsPage();
            TestLogger.logInfo(userType, "On products page: " + onProductsPage);
            assertTrue(onProductsPage, "Error user should be able to login successfully");
            
            // Try adding product to cart - Known issue: cart icon doesn't update or shows inconsistent behavior
            TestLogger.logTestStep(userType, "Adding product to cart: " + TEST_PRODUCT);
            productsPage.clickAddToCartForProduct(TEST_PRODUCT);
            int cartCount = productsPage.getCartCount();
            TestLogger.logInfo(userType, "Cart count after adding product: " + cartCount);
            
            // Go to cart and check if product was added correctly
            TestLogger.logTestStep(userType, "Navigating to cart page");
            CartPage cartPage = productsPage.goToCart();
            
            // The error_user should have issues with cart functionality
            TestLogger.logTestStep(userType, "Checking if product is in cart");
            boolean productInCart = cartPage.isProductInCart(TEST_PRODUCT);
            TestLogger.logInfo(userType, "Product is in cart: " + productInCart);
            
            // This is expected behavior for error_user
            if (!productInCart) {
                TestLogger.logInfo(userType, "Error user cart functionality issue detected: Product not in cart as expected");
            } else {
                TestLogger.logInfo(userType, "Product was added to cart despite being error_user");
            }
            
            TestLogger.logTestEnd(userType, "Error User Login & Cart Functionality Test", true, 
                                 productInCart ? "Product was found in cart" : "Product was not in cart (expected issue)");
        } catch (Exception e) {
            TestLogger.logError(userType, "Test failed with exception", e);
            TestLogger.logTestEnd(userType, "Error User Login & Cart Functionality Test", false, "Test failed: " + e.getMessage());
            fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * TC-106: Visual User Login
     * Tests that visual_user can login but may see visual inconsistencies
     */
    @Test
    @DisplayName("TC-106: Visual User Login")
    public void testVisualUser() {
        UserType userType = UserType.VISUAL_USER;
        TestLogger.logTestStart(userType, "Visual User Login Test");
        
        try {
            // Login as visual user
            TestLogger.logTestStep(userType, "Logging in as visual user");
            ProductsPage productsPage = loginPage.loginAs(userType.getUsername(), LoginPage.PASSWORD);
            
            // Verify user is on products page
            TestLogger.logTestStep(userType, "Verifying redirect to products page");
            boolean onProductsPage = productsPage.isOnProductsPage();
            TestLogger.logInfo(userType, "On products page: " + onProductsPage);
            assertTrue(onProductsPage, "Visual user should be able to login successfully");
            
            // Visual user should be able to add products to cart
            TestLogger.logTestStep(userType, "Adding product to cart: " + TEST_PRODUCT);
            productsPage.clickAddToCartForProduct(TEST_PRODUCT);
            int cartCount = productsPage.getCartCount();
            TestLogger.logInfo(userType, "Cart count after adding product: " + cartCount);
            assertEquals(1, cartCount, "Visual user should be able to add product to cart");
            
            // Note: Visual inconsistencies are difficult to test programmatically
            TestLogger.logInfo(userType, "Visual user test complete - manual verification of visual elements recommended");
            
            TestLogger.logTestEnd(userType, "Visual User Login Test", true, "All steps completed successfully");
        } catch (Exception e) {
            TestLogger.logError(userType, "Test failed with exception", e);
            TestLogger.logTestEnd(userType, "Visual User Login Test", false, "Test failed: " + e.getMessage());
            fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * Run all tests for a specific user type
     * This method can be used to test a single user type
     */
    public void runAllTestsForUserType(UserType userType) {
        TestLogger.logInfo(userType, "Starting all tests for user type: " + userType.getUsername());
        
        switch (userType) {
            case STANDARD_USER:
                testStandardUser();
                break;
            case LOCKED_OUT_USER:
                testLockedOutUser();
                break;
            case PROBLEM_USER:
                testProblemUser();
                break;
            case PERFORMANCE_GLITCH_USER:
                testPerformanceGlitchUser();
                break;
            case ERROR_USER:
                testErrorUser();
                break;
            case VISUAL_USER:
                testVisualUser();
                break;
            default:
                TestLogger.logWarning(userType, "Unknown user type: " + userType.getUsername());
        }
        
        TestLogger.logInfo(userType, "Completed all tests for user type: " + userType.getUsername());
    }
}
