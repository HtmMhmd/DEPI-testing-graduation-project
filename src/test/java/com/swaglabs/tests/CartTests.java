package com.swaglabs.tests;

import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.WebDriverManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Swag Labs cart functionality
 * Authors: Omar Shahin, Hatem Mohamed
 */
public class CartTests {
    
    private ProductsPage productsPage;
    private static final String TEST_PRODUCT1 = "Sauce Labs Backpack";
    private static final String TEST_PRODUCT2 = "Sauce Labs Bike Light";
    
    @BeforeEach
    public void setup() {
        WebDriverManager.setupDriver();
        WebDriverManager.navigateToBaseUrl();
        
        // Login and navigate to products page
        LoginPage loginPage = new LoginPage();
        productsPage = loginPage.loginAs(LoginPage.STANDARD_USER, LoginPage.PASSWORD);
    }
    
    @AfterEach
    public void tearDown() {
        WebDriverManager.quitDriver();
    }
    
    /**
     * TC-011: Add Product to Cart
     * Steps:
     * 1. View product listing
     * 2. Click "Add to Cart" button for a product
     * Expected Result: Product added to cart, cart counter updated
     * Priority: High
     */
    @Test
    @DisplayName("TC-011: Add Product to Cart")
    public void testAddProductToCart() {
        // Add product to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
        
        // Verify cart counter is updated
        assertEquals(1, productsPage.getCartCount(), "Cart count should be 1 after adding product");
        
        // Navigate to cart page and verify product is in cart
        CartPage cartPage = productsPage.goToCart();
        assertTrue(cartPage.isProductInCart(TEST_PRODUCT1), 
                "Product should be in cart after clicking Add to Cart button");
    }
    
    /**
     * TC-012: Add Multiple Products to Cart
     * Steps:
     * 1. View product listing
     * 2. Click "Add to Cart" for multiple products
     * Expected Result: All products added to cart, cart counter shows correct count
     * Priority: High
     */
    @Test
    @DisplayName("TC-012: Add Multiple Products to Cart")
    public void testAddMultipleProductsToCart() {
        // Add multiple products to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT1)
                   .clickAddToCartForProduct(TEST_PRODUCT2);
        
        // Verify cart counter is updated
        assertEquals(2, productsPage.getCartCount(), "Cart count should be 2 after adding two products");
        
        // Navigate to cart page and verify both products are in cart
        CartPage cartPage = productsPage.goToCart();
        List<String> cartItems = cartPage.getCartItemNames();
        
        assertTrue(cartItems.contains(TEST_PRODUCT1), "First product should be in cart");
        assertTrue(cartItems.contains(TEST_PRODUCT2), "Second product should be in cart");
        assertEquals(2, cartItems.size(), "Cart should contain exactly 2 items");
    }
    
    /**
     * TC-013: Remove Product from Cart
     * Steps:
     * 1. Add product to cart
     * 2. Go to cart page
     * 3. Click "Remove" button for a product
     * Expected Result: Product removed from cart
     * Priority: High
     */
    @Test
    @DisplayName("TC-013: Remove Product from Cart")
    public void testRemoveProductFromCart() {
        // Add product to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
        
        // Go to cart page
        CartPage cartPage = productsPage.goToCart();
        
        // Verify product is in cart
        assertTrue(cartPage.isProductInCart(TEST_PRODUCT1), "Product should be in cart initially");
        
        // Remove product from cart
        cartPage.removeItem(TEST_PRODUCT1);
        
        // Verify product is removed
        assertEquals(0, cartPage.getNumberOfItemsInCart(), "Cart should be empty after removing the product");
    }
    
    /**
     * TC-014: Update Cart from Product Page
     * Steps:
     * 1. Add product to cart
     * 2. Click "Remove" button on product page
     * Expected Result: Product removed from cart
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-014: Update Cart from Product Page")
    public void testUpdateCartFromProductPage() {
        // Add product to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
        
        // Verify cart counter is updated
        assertEquals(1, productsPage.getCartCount(), "Cart count should be 1 after adding product");
        
        // Remove product from product listing page
        productsPage.clickRemoveForProduct(TEST_PRODUCT1);
        
        // Verify cart counter is updated
        assertEquals(0, productsPage.getCartCount(), "Cart count should be 0 after removing product");
        
        // Navigate to cart page and verify it's empty
        CartPage cartPage = productsPage.goToCart();
        assertEquals(0, cartPage.getNumberOfItemsInCart(), "Cart should be empty");
    }
    
    /**
     * TC-015: Continue Shopping
     * Steps:
     * 1. Add products to cart
     * 2. Navigate to cart page
     * 3. Click "Continue Shopping" button
     * Expected Result: User redirected back to products page
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-015: Continue Shopping")
    public void testContinueShopping() {
        // Add product to cart and navigate to cart page
        productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
        CartPage cartPage = productsPage.goToCart();
        
        // Click continue shopping button
        ProductsPage redirectedPage = cartPage.continueShopping();
        
        // Verify user is redirected to products page
        assertTrue(redirectedPage.isOnProductsPage(), 
                "User should be redirected to products page after clicking Continue Shopping");
    }
    
    /**
     * TC-016: Cart Persistence
     * Steps:
     * 1. Add products to cart
     * 2. Navigate to product details page
     * 3. Return to products page
     * Expected Result: Cart contents remain unchanged
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-016: Cart Persistence")
    public void testCartPersistence() {
        // Add product to cart
        productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
        int initialCartCount = productsPage.getCartCount();
        
        // Navigate to product details page and back
        productsPage.clickOnProduct(TEST_PRODUCT2)
                   .backToProducts();
        
        // Verify cart count remains the same
        assertEquals(initialCartCount, productsPage.getCartCount(), 
                "Cart count should remain unchanged after navigation");
    }
}