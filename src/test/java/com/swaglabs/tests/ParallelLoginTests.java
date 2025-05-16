package com.swaglabs.tests;

import com.swaglabs.pages.ParallelLoginPage;
import com.swaglabs.utils.ThreadLocalWebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.swaglabs.utils.TestListener; // Corrected import
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test runner for parallel execution of all login tests
 * Demonstrates using the ThreadLocalWebDriverManager and screenshot capture
 */
@ExtendWith(TestListener.class) // Corrected class name
public class ParallelLoginTests {
    
    private ParallelLoginPage loginPage;
    
    @BeforeEach
    public void setup() {
        ThreadLocalWebDriverManager.setupDriver();
        ThreadLocalWebDriverManager.navigateToBaseUrl();
        loginPage = new ParallelLoginPage();
    }
    
    @AfterEach
    public void tearDown() {
        ThreadLocalWebDriverManager.quitDriver();
    }
    
    /**
     * TC-001: Valid Login - Parallel Execution
     */
    @Test
    @DisplayName("TC-001-P: Valid Login (Parallel)")
    public void testValidLogin() {
        // Perform login with valid credentials
        ProductsPage productsPage = loginPage.loginAs(LoginPage.STANDARD_USER, LoginPage.PASSWORD);
        
        // Verify user is redirected to products page
        assertTrue(productsPage.isOnProductsPage(), "User should be redirected to products page after successful login");
    }
    
    /**
     * TC-002: Invalid Login - Wrong Password - Parallel Execution
     */
    @Test
    @DisplayName("TC-002-P: Invalid Login - Wrong Password (Parallel)")
    public void testInvalidLoginWrongPassword() {
        // Perform login with invalid credentials (wrong password)
        loginPage.loginAs(LoginPage.STANDARD_USER, "wrong_password");
        
        // Verify error message is displayed and user remains on login page
        assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for invalid password");
        assertTrue(loginPage.isOnLoginPage(), "User should remain on login page after failed login");
    }
    
    /**
     * TC-003: Invalid Login - Empty Fields - Parallel Execution
     */
    @Test
    @DisplayName("TC-003-P: Invalid Login - Empty Fields (Parallel)")
    public void testInvalidLoginEmptyFields() {
        // Click login button without entering credentials
        loginPage.clickLoginButton();
        
        // Verify error message is displayed and user remains on login page
        assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for empty fields");
        assertTrue(loginPage.isOnLoginPage(), "User should remain on login page when fields are empty");
    }
    
    /**
     * TC-004: Login with Locked User - Parallel Execution
     */
    @Test
    @DisplayName("TC-004-P: Login with Locked User (Parallel)")
    public void testLoginWithLockedUser() {
        // Perform login with locked out user
        loginPage.loginAs(LoginPage.LOCKED_OUT_USER, LoginPage.PASSWORD);
        
        // Verify error message is displayed
        assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for locked out user");
        assertTrue(loginPage.getErrorMessage().contains("locked"), 
                "Error message should indicate that user is locked out");
    }
}
