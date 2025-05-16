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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import com.swaglabs.enums.UserType;

/**
 * Test cases for different user types in Swag Labs application
 * Authors: Omar Shahin, Hatem Mohamed
 */
public class UserTypeTests {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage; // Added for use across tests
    private CartPage cartPage; // Added for use across tests
    private CheckoutPage checkoutPage; // Added for use across tests

    // Test Products
    private static final String TEST_PRODUCT = "Sauce Labs Backpack";

    @BeforeEach
    public void setup() {
        WebDriverManager.setupDriver();
        WebDriverManager.navigateToBaseUrl();
        driver = WebDriverManager.getDriver();
        loginPage = new LoginPage();
    }

    @AfterEach
    public void tearDown() {
        WebDriverManager.quitDriver();
    }

    /**
     * TC-101: Standard User Login and Checkout
     * Description: Standard user can successfully login, add to cart, and complete checkout.
     * Priority: High
     */
    @Test
    @DisplayName("TC-101: Standard User Login and Checkout")
    public void testStandardUserLoginAndCheckout() {
        productsPage = loginPage.loginAs(UserType.STANDARD_USER.getUsername(), UserType.STANDARD_USER.getPassword());
        assertTrue(productsPage.isOnProductsPage(), "Standard user should be able to login successfully");

        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        assertEquals(1, productsPage.getCartCount(), "Standard user should be able to add product to cart");

        cartPage = productsPage.goToCart();
        assertTrue(cartPage.isProductInCart(TEST_PRODUCT), "Product should be in cart");

        checkoutPage = cartPage.checkout();
        checkoutPage.enterFirstName("Test")
                    .enterLastName("User")
                    .enterZipCode("12345")
                    .clickContinue();
        assertTrue(checkoutPage.isOnCheckoutStepTwo(), "Should be on checkout overview page");

        checkoutPage.clickFinish();
        assertTrue(checkoutPage.isOnCheckoutComplete(), "Order not completed for standard_user.");
    }

    /**
     * TC-102: Locked Out User Login
     * Description: Locked out user cannot login
     * Priority: High
     */
    @Test
    @DisplayName("TC-102: Locked Out User Login")
    public void testLockedOutUserLogin() {
        loginPage.loginAs(UserType.LOCKED_OUT_USER.getUsername(), UserType.LOCKED_OUT_USER.getPassword());
        assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for locked out user");
        assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"), "Error message should indicate user is locked out");
        assertTrue(loginPage.isOnLoginPage(), "User should remain on login page");
    }

    /**
     * TC-103: Problem User Login and UI Issues
     * Description: Problem user can login but experiences UI issues (images, sorting).
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-103: Problem User Login and UI Issues")
    public void testProblemUserLoginAndUIIssues() {
        productsPage = loginPage.loginAs(UserType.PROBLEM_USER.getUsername(), UserType.PROBLEM_USER.getPassword());
        assertTrue(productsPage.isOnProductsPage(), "Problem user should be able to login successfully");

        List<WebElement> productImages = driver.findElements(By.cssSelector(".inventory_item_img img"));
        if (!productImages.isEmpty()) {
            String firstImageSrc = productImages.get(0).getAttribute("src");
            boolean allImagesSame = productImages.stream().allMatch(img -> img.getAttribute("src").equals(firstImageSrc));
            assertTrue(allImagesSame, "Problem user should see the same image for all products");
        }

        String firstProductNameBefore = productsPage.getProductNames().get(0);
        productsPage.sortProductsBy("Name (Z to A)");
        String firstProductNameAfter = productsPage.getProductNames().get(0);
        assertEquals(firstProductNameBefore, firstProductNameAfter, "Sorting should not work properly for problem user");
    }

    /**
     * TC-104: Performance Glitch User Login
     * Description: Performance glitch user experiences slow performance
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-104: Performance Glitch User Login")
    public void testPerformanceGlitchUserLogin() {
        long startTime = System.currentTimeMillis();
        productsPage = loginPage.loginAs(UserType.PERFORMANCE_GLITCH_USER.getUsername(), UserType.PERFORMANCE_GLITCH_USER.getPassword());
        long endTime = System.currentTimeMillis();
        long loginDuration = endTime - startTime;

        assertTrue(productsPage.isOnProductsPage(), "Performance glitch user should be able to login");
        System.out.println("Login duration for performance_glitch_user: " + loginDuration + "ms");
        // Add assertion for login duration if specific threshold is defined, e.g., assertTrue(loginDuration > 2000, "Login was not slow enough");
    }

    /**
     * TC-105: Error User - Add to Cart Error
     * Description: Error user encounters errors when trying to add items to cart.
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-105: Error User - Add to Cart Error")
    public void testErrorUserAddToCart() {
        productsPage = loginPage.loginAs(UserType.ERROR_USER.getUsername(), UserType.ERROR_USER.getPassword());
        assertTrue(productsPage.isOnProductsPage(), "Error user should be able to login");

        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        // Assert that cart count does not increase or an error message is shown (specifics depend on app behavior)
        // This might require checking for an error message on the product page or verifying cart count remains 0.
        // For now, we'll assume the cart count doesn't change as a basic check.
        assertEquals(0, productsPage.getCartCount(), "Cart count should not increase for error_user when adding to cart.");
        // It would be better to check for a specific error message if one appears.
    }

    /**
     * TC-106: Visual User - Visual Test (Placeholder)
     * Description: Visual user login for visual regression testing (manual or with visual testing tools).
     * Priority: Low
     */
    @Test
    @DisplayName("TC-106: Visual User - Visual Test")
    public void testVisualUserLogin() {
        productsPage = loginPage.loginAs(UserType.VISUAL_USER.getUsername(), UserType.VISUAL_USER.getPassword());
        assertTrue(productsPage.isOnProductsPage(), "Visual user should be able to login");
        // This test would typically involve visual snapshot comparison using a specialized tool.
        // For now, it just verifies login.
        System.out.println("Visual user logged in. Manual visual check or visual testing tool would be used here.");
    }

    /**
     * Parameterized test for the checkout process for different user types.
     * Excludes problem_user as their checkout behavior is erratic and tested separately.
     */
    @ParameterizedTest
    @EnumSource(value = UserType.class, names = {"STANDARD_USER", "PERFORMANCE_GLITCH_USER", "ERROR_USER", "VISUAL_USER"})
    @DisplayName("Parameterized Checkout Process")
    void testCheckoutProcessForEachUserType(UserType userType) {
        productsPage = loginPage.loginAs(userType.getUsername(), userType.getPassword());
        assertTrue(productsPage.isOnProductsPage(), "User " + userType + " should be able to login.");

        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        
        // For error_user, adding to cart might fail. We proceed if cart has items.
        if (productsPage.getCartCount() > 0 || !userType.equals(UserType.ERROR_USER)) {
            cartPage = productsPage.goToCart();
            assertTrue(cartPage.isProductInCart(TEST_PRODUCT), "Product should be in cart for " + userType);

            checkoutPage = cartPage.checkout();
            checkoutPage.enterFirstName("Test")
                        .enterLastName("User")
                        .enterZipCode("12345")
                        .clickContinue();
            assertTrue(checkoutPage.isOnCheckoutStepTwo(), "Not on checkout step two for " + userType);

            checkoutPage.clickFinish();
            assertTrue(checkoutPage.isOnCheckoutComplete(), "Order not completed for " + userType);
        } else if (userType.equals(UserType.ERROR_USER)) {
            System.out.println("Skipping checkout for ERROR_USER as add to cart likely failed as expected.");
            // Optionally, assert that an error message related to add to cart was shown.
        }
    }

    /**
     * Test specific error conditions for the problem_user during checkout.
     * For example, trying to checkout with missing information.
     */
    @Test
    @DisplayName("Problem User - Checkout Last Name Error")
    void testProblemUserLastNameErrorDuringCheckout() {
        productsPage = loginPage.loginAs(UserType.PROBLEM_USER.getUsername(), UserType.PROBLEM_USER.getPassword());
        assertTrue(productsPage.isOnProductsPage(), "Problem user should be able to login.");

        productsPage.clickAddToCartForProduct(TEST_PRODUCT);
        cartPage = productsPage.goToCart();
        checkoutPage = cartPage.checkout();

        checkoutPage.enterFirstName("Problem")
                    .enterLastName("") // Intentionally leave blank
                    .enterZipCode("54321");
        checkoutPage.clickContinue();

        assertTrue(checkoutPage.isErrorDisplayed(), "Error message should be displayed for missing last name.");
        assertTrue(checkoutPage.getErrorMessage().toLowerCase().contains("last name is required"),
                   "Error message for missing last name not shown for problem_user.");
    }
}