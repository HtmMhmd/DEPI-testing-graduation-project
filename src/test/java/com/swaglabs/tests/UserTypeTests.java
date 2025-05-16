package com.swaglabs.tests;

import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.CheckoutPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for different user types in Swag Labs application
 * Authors: Omar Shahin, Hatem Mohamed
 */
public class UserTypeTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    
    // Test Products
    private static final String TEST_PRODUCT = "Sauce Labs Backpack";
    
    // User types
    private static final String STANDARD_USER = "standard_user";
    private static final String LOCKED_OUT_USER = "locked_out_user";
    private static final String PROBLEM_USER = "problem_user";
    private static final String PERFORMANCE_GLITCH_USER = "performance_glitch_user";
    private static final String ERROR_USER = "error_user";
    private static final String VISUAL_USER = "visual_user";
    private static final String PASSWORD = "secret_sauce";
    
    @BeforeEach
    public void setup() {
        WebDriverManager.setupDriver();
        WebDriverManager.navigateToBaseUrl();
        driver = WebDriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Increased timeout for performance_glitch_user
        loginPage = new LoginPage();
    }
    
    @AfterEach
    public void tearDown() {
        WebDriverManager.quitDriver();
    }
    
    /**
     * TC-101: Standard User Login
     * Description: Standard user can successfully login and access all features
     * Priority: High
     */
    @Test
    @DisplayName("TC-101: Standard User Login")
    public void testStandardUserLogin() {
        // Login as standard user
        ProductsPage productsPage = loginPage.loginAs(STANDARD_USER, PASSWORD);
        
        // Verify successful login
        assertTrue(productsPage.isOnProductsPage(), 
                "Standard user should be able to login successfully");
        
        // Add product to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        assertEquals(1, productsPage.getCartCount(), 
                "Standard user should be able to add product to cart");
        
        // Complete checkout process
        CartPage cartPage = productsPage.goToCart();
        assertTrue(cartPage.isProductInCart(TEST_PRODUCT), 
                "Product should be in cart");
        
        CheckoutPage checkoutPage = cartPage.checkout();
        checkoutPage.enterFirstName("John")
                   .enterLastName("Doe")
                   .enterPostalCode("12345")
                   .clickContinue();
                   
        assertTrue(checkoutPage.isOnCheckoutOverviewPage(), 
                "Standard user should be able to proceed to checkout overview");
        
        checkoutPage.finish();
        assertTrue(checkoutPage.isOnCheckoutComplete(), 
                "Standard user should be able to complete checkout");
    }
    
    /**
     * TC-102: Locked Out User Login
     * Description: Locked out user cannot login
     * Priority: High
     */
    @Test
    @DisplayName("TC-102: Locked Out User Login")
    public void testLockedOutUserLogin() {
        // Attempt to login as locked out user
        loginPage.loginAs(LOCKED_OUT_USER, PASSWORD);
        
        // Verify error message
        assertTrue(loginPage.isLoginErrorDisplayed(), 
                "Error message should be displayed for locked out user");
        assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"), 
                "Error message should indicate user is locked out");
        assertTrue(loginPage.isOnLoginPage(), 
                "User should remain on login page");
    }
    
    /**
     * TC-103: Problem User Login
     * Description: Problem user can login but experiences UI issues
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-103: Problem User Login")
    public void testProblemUserLogin() {
        // Login as problem user
        ProductsPage productsPage = loginPage.loginAs(PROBLEM_USER, PASSWORD);
        
        // Verify successful login
        assertTrue(productsPage.isOnProductsPage(), 
                "Problem user should be able to login successfully");
        
        // Check for image issues (all product images should be the same)
        List<WebElement> productImages = driver.findElements(By.cssSelector(".inventory_item_img img"));
        String firstImageSrc = productImages.get(0).getAttribute("src");
        
        boolean allImagesSame = true;
        for (WebElement image : productImages) {
            if (!image.getAttribute("src").equals(firstImageSrc)) {
                allImagesSame = false;
                break;
            }
        }
        
        assertTrue(allImagesSame, 
                "Problem user should see the same image for all products");
        
        // Try to sort products and verify it doesn't work properly
        String firstProductNameBefore = productsPage.getProductNames().get(0);
        productsPage.sortProductsBy("Name (Z to A)");
        String firstProductNameAfter = productsPage.getProductNames().get(0);
        
        assertEquals(firstProductNameBefore, firstProductNameAfter, 
                "Sorting should not work properly for problem user");
    }
    
    /**
     * TC-104: Performance Glitch User Login
     * Description: Performance glitch user experiences slow performance
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-104: Performance Glitch User Login")
    public void testPerformanceGlitchUserLogin() {
        // Record start time
        long startTime = System.currentTimeMillis();
        
        // Login as performance glitch user
        ProductsPage productsPage = loginPage.loginAs(PERFORMANCE_GLITCH_USER, PASSWORD);
        
        // Record end time
        long endTime = System.currentTimeMillis();
        long loginDuration = endTime - startTime;
        
        // Verify successful login (with delay)
        assertTrue(productsPage.isOnProductsPage(), 
                "Performance glitch user should be able to login successfully");
        
        // Assert that login took longer than normal (typically over 2 seconds)
        assertTrue(loginDuration > 2000, 
                "Login should take longer than 2 seconds for performance glitch user");
        
        System.out.println("Performance glitch user login took " + loginDuration + " ms");
    }
    
    /**
     * TC-105: Error User Login
     * Description: Error user can login but encounters errors during checkout
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-105: Error User Login")
    public void testErrorUserLogin() {
        // Login as error user
        ProductsPage productsPage = loginPage.loginAs(ERROR_USER, PASSWORD);
        
        // Verify successful login
        assertTrue(productsPage.isOnProductsPage(), 
                "Error user should be able to login successfully");
        
        // Add product to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        assertEquals(1, productsPage.getCartCount(), 
                "Error user should be able to add product to cart");
        
        // Navigate to cart and attempt checkout
        CartPage cartPage = productsPage.goToCart();
        assertTrue(cartPage.isProductInCart(TEST_PRODUCT), 
                "Product should be in cart");
        
        // Attempt to remove item from cart
        cartPage.removeItem(TEST_PRODUCT);
        
        // Verify item is still in cart (error user can't remove items)
        assertTrue(cartPage.isProductInCart(TEST_PRODUCT), 
                "Error user should not be able to remove items from cart");
    }
    
    /**
     * TC-106: Visual User Login
     * Description: Visual user can login but encounters visual glitches
     * Priority: Low
     */
    @Test
    @DisplayName("TC-106: Visual User Login")
    public void testVisualUserLogin() {
        // Login as visual user
        ProductsPage productsPage = loginPage.loginAs(VISUAL_USER, PASSWORD);
        
        // Verify successful login
        assertTrue(productsPage.isOnProductsPage(), 
                "Visual user should be able to login successfully");
        
        // Add product to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        assertEquals(1, productsPage.getCartCount(), 
                "Visual user should be able to add product to cart");
        
        // Complete checkout process
        CartPage cartPage = productsPage.goToCart();
        CheckoutPage checkoutPage = cartPage.checkout();
        checkoutPage.enterFirstName("John")
                   .enterLastName("Doe")
                   .enterPostalCode("12345")
                   .clickContinue();
                   
        assertTrue(checkoutPage.isOnCheckoutOverviewPage(), 
                "Visual user should be able to proceed to checkout overview");
        
        // Verify there are visual oddities on the page
        // Look for specific visual elements that might be affected
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        if (!priceElements.isEmpty()) {
            String priceText = priceElements.get(0).getText();
            // For visual user, prices may have visual issues but functionality remains
            assertNotNull(priceText, "Price text should be present even with visual issues");
        }
        
        checkoutPage.finish();
        assertTrue(checkoutPage.isOnCheckoutComplete(), 
                "Visual user should be able to complete checkout");
    }
}