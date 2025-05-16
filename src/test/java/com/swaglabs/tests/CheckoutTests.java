package com.swaglabs.tests;

import com.swaglabs.pages.*;
import com.swaglabs.utils.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Swag Labs checkout process functionality
 * Authors: Omar Shahin, Hatem Mohamed
 */
public class CheckoutTests {
    
    private WebDriver driver; // Add WebDriver field declaration
    private ProductsPage productsPage;
    private CartPage cartPage;
    private static final String TEST_PRODUCT = "Sauce Labs Backpack";
    
    @BeforeEach
    public void setup() {
        WebDriverManager.setupDriver();
        WebDriverManager.navigateToBaseUrl();
        driver = WebDriverManager.getDriver(); // Initialize the WebDriver
        
        // Login, add a product to cart, and navigate to cart page
        LoginPage loginPage = new LoginPage();
        productsPage = loginPage.loginAs(LoginPage.STANDARD_USER, LoginPage.PASSWORD);
        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        cartPage = productsPage.goToCart();
    }
    
    @AfterEach
    public void tearDown() {
        WebDriverManager.quitDriver();
    }
    
    /**
     * TC-017: Proceed to Checkout
     * Steps:
     * 1. Add products to cart
     * 2. Navigate to cart page
     * 3. Click "Checkout" button
     * Expected Result: Checkout form displayed
     * Priority: High
     */
    @Test
    @DisplayName("TC-017: Proceed to Checkout")
    public void testProceedToCheckout() {
        // Click checkout button
        CheckoutPage checkoutPage = cartPage.checkout();
        
        // Verify checkout form is displayed
        assertTrue(checkoutPage.isOnCheckoutStepOne(), 
                "Checkout form should be displayed after clicking Checkout button");
    }
    
    /**
     * TC-018: Enter Customer Information
     * Steps:
     * 1. Proceed to checkout
     * 2. Enter first name, last name, and zip code
     * 3. Click "Continue"
     * Expected Result: Payment and shipping information page displayed
     * Priority: High
     */
    @Test
    @DisplayName("TC-018: Enter Customer Information")
    public void testEnterCustomerInformation() {
        // Proceed to checkout
        CheckoutPage checkoutPage = cartPage.checkout();
        
        // Enter customer information and continue
        checkoutPage.fillCustomerInfo("John", "Doe", "12345")
                   .clickContinue();
        
        // Verify checkout overview page is displayed
        assertTrue(checkoutPage.isOnCheckoutStepTwo(), 
                "Checkout overview page should be displayed after entering customer information");
    }
    
    /**
     * TC-019: Invalid Customer Information
     * Steps:
     * 1. Proceed to checkout
     * 2. Leave required fields empty
     * 3. Click "Continue"
     * Expected Result: Validation errors displayed
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-019: Invalid Customer Information")
    public void testInvalidCustomerInformation() {
        // Proceed to checkout
        CheckoutPage checkoutPage = cartPage.checkout();
        
        // Click continue without entering any information
        checkoutPage.clickContinue();
        
        // Verify error message is displayed
        assertTrue(checkoutPage.isErrorDisplayed(), 
                "Error message should be displayed for empty required fields");
    }
    
    /**
     * TC-020: Order Summary
     * Steps:
     * 1. Complete customer information
     * 2. View checkout overview page
     * Expected Result: Order summary displayed with correct items and total
     * Priority: High
     */
    @Test
    @DisplayName("TC-020: Order Summary")
    public void testOrderSummary() {
        try {
            // Verify that we're on the cart page and cart has items
            assertTrue(cartPage.isOnCartPage(), 
                      "Should be on cart page before proceeding to checkout");
            assertTrue(cartPage.getNumberOfItemsInCart() > 0,
                      "Cart should contain at least one item");
            
            // Print debug info about the checkout button
            try {
                WebElement checkoutBtn = driver.findElement(By.id("checkout"));
                System.out.println("Checkout button found: " + checkoutBtn.isDisplayed() + ", " + checkoutBtn.isEnabled());
            } catch (Exception e) {
                System.out.println("Couldn't find checkout button for debug info: " + e.getMessage());
                // Try to print the page source for debugging
                System.out.println("Page source excerpt: " + 
                   driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length())));
            }
            
            // Proceed to checkout and fill customer information
            CheckoutPage checkoutPage = cartPage.checkout();
            checkoutPage.fillCustomerInfo("John", "Doe", "12345")
                       .clickContinue();
            
            // Verify order summary information
            assertTrue(checkoutPage.isOnCheckoutStepTwo(), 
                    "Should be on checkout overview page");
            
            double subtotal = checkoutPage.getSubtotalAmount();
            double tax = checkoutPage.getTaxAmount();
            double total = checkoutPage.getTotalAmount();
            
            // Log the values for debugging
            System.out.println("Subtotal: $" + subtotal);
            System.out.println("Tax: $" + tax);
            System.out.println("Total: $" + total);
            
            // Verify total equals subtotal + tax (with small delta for floating point comparison)
            assertTrue(subtotal > 0, "Subtotal should be greater than 0");
            assertTrue(tax > 0, "Tax should be greater than 0");
            assertEquals(subtotal + tax, total, 0.001, 
                    "Total amount should equal subtotal plus tax");
        } catch (Exception e) {
            // Take a screenshot and log the error
            WebDriverManager.captureScreenshot("OrderSummaryError");
            System.err.println("Exception in testOrderSummary: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * TC-021: Complete Purchase
     * Steps:
     * 1. View order summary
     * 2. Click "Finish" button
     * Expected Result: Order confirmation page displayed
     * Priority: High
     */
    @Test
    @DisplayName("TC-021: Complete Purchase")
    public void testCompletePurchase() {
        // Proceed to checkout, fill customer information, and continue to order summary
        CheckoutPage checkoutPage = cartPage.checkout();
        checkoutPage.fillCustomerInfo("John", "Doe", "12345")
                   .clickContinue();
        
        // Complete purchase by clicking finish button
        checkoutPage.clickFinish();
        
        // Verify order confirmation page is displayed
        assertTrue(checkoutPage.isOnCheckoutComplete(), 
                "Order confirmation page should be displayed after completing purchase");
    }
    
    /**
     * TC-022: Navigate Back to Products
     * Steps:
     * 1. Complete purchase
     * 2. Click "Back Home" button
     * Expected Result: User redirected to products page
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-022: Navigate Back to Products")
    public void testNavigateBackToProducts() {
        // Proceed to checkout, fill customer information, continue to order summary, and complete purchase
        CheckoutPage checkoutPage = cartPage.checkout();
        checkoutPage.fillCustomerInfo("John", "Doe", "12345")
                   .clickContinue()
                   .clickFinish();
        
        // Click back home button
        ProductsPage redirectedPage = checkoutPage.clickBackHome();
        
        // Verify user is redirected to products page
        assertTrue(redirectedPage.isOnProductsPage(), 
                "User should be redirected to products page after clicking Back Home button");
    }
}