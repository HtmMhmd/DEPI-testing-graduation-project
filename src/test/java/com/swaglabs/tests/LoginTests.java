package com.swaglabs.tests;

import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.WebDriverManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Swag Labs login functionality
 * Authors: Omar Shahin, Hatem Mohamed
 */
public class LoginTests {
    
    private LoginPage loginPage;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.setupDriver();
        WebDriverManager.navigateToBaseUrl();
        loginPage = new LoginPage();
    }
    
    @AfterEach
    public void tearDown() {
        WebDriverManager.quitDriver();
    }
    
    /**
     * TC-001: Valid Login
     * Steps:
     * 1. Navigate to login page
     * 2. Enter valid username and password
     * 3. Click Login button
     * Expected Result: User is logged in and redirected to products page
     * Priority: High
     */
    @Test
    @DisplayName("TC-001: Valid Login")
    public void testValidLogin() {
        // Perform login with valid credentials
        ProductsPage productsPage = loginPage.loginAs(LoginPage.STANDARD_USER, LoginPage.PASSWORD);
        
        // Verify user is redirected to products page
        assertTrue(productsPage.isOnProductsPage(), "User should be redirected to products page after successful login");
    }
    
    /**
     * TC-002: Invalid Login - Wrong Password
     * Steps:
     * 1. Navigate to login page
     * 2. Enter valid username but incorrect password
     * 3. Click Login button
     * Expected Result: Error message displayed, user remains on login page
     * Priority: High
     */
    @Test
    @DisplayName("TC-002: Invalid Login - Wrong Password")
    public void testInvalidLoginWrongPassword() {
        // Perform login with invalid credentials (wrong password)
        loginPage.loginAs(LoginPage.STANDARD_USER, "wrong_password");
        
        // Verify error message is displayed and user remains on login page
        assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for invalid password");
        assertTrue(loginPage.isOnLoginPage(), "User should remain on login page after failed login");
    }
    
    /**
     * TC-003: Invalid Login - Empty Fields
     * Steps:
     * 1. Navigate to login page
     * 2. Leave username and password fields empty
     * 3. Click Login button
     * Expected Result: Validation message displayed, user remains on login page
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-003: Invalid Login - Empty Fields")
    public void testInvalidLoginEmptyFields() {
        // Click login button without entering credentials
        loginPage.clickLoginButton();
        
        // Verify error message is displayed and user remains on login page
        assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for empty fields");
        assertTrue(loginPage.isOnLoginPage(), "User should remain on login page when fields are empty");
    }
    
    /**
     * TC-027: Login with Locked User
     * Steps:
     * 1. Attempt to login with locked out user credentials
     * Expected Result: Appropriate error message displayed
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-027: Login with Locked User")
    public void testLoginWithLockedUser() {
        // Perform login with locked out user
        loginPage.loginAs(LoginPage.LOCKED_OUT_USER, LoginPage.PASSWORD);
        
        // Verify error message is displayed and contains "locked out" text
        assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for locked user");
        assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"), 
                "Error message should indicate that user is locked out");
    }
}