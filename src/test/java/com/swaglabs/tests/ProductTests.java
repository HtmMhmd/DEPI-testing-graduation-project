package com.swaglabs.tests;

import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductDetailsPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.WebDriverManager;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Swag Labs product browsing functionality
 * Authors: Omar Shahin, Hatem Mohamed
 */
public class ProductTests {
    
    private ProductsPage productsPage;
    private static final String TEST_PRODUCT = "Sauce Labs Backpack";
    
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
     * TC-004: View Products List
     * Steps:
     * 1. Login to application
     * Expected Result: Products page displayed with list of products
     * Priority: High
     */
    @Test
    @DisplayName("TC-004: View Products List")
    public void testViewProductsList() {
        // Verify products page is displayed after login
        assertTrue(productsPage.isOnProductsPage(), "Products page should be displayed after login");
        
        // Verify product list is not empty
        List<String> productNames = productsPage.getProductNames();
        assertFalse(productNames.isEmpty(), "Product list should not be empty");
    }
    
    /**
     * TC-005: Sort Products by Name A-Z
     * Steps:
     * 1. View products listing
     * 2. Select "Name (A to Z)" from sort dropdown
     * Expected Result: Products sorted alphabetically A-Z
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-005: Sort Products by Name A-Z")
    public void testSortProductsByNameAZ() {
        // Sort products by name A-Z
        productsPage.sortProductsBy("Name (A to Z)");
        
        // Get the product names after sorting
        List<String> actualProductNames = productsPage.getProductNames();
        
        // Create a copy of the list and sort it alphabetically
        List<String> expectedSortedNames = new ArrayList<>(actualProductNames);
        expectedSortedNames.sort(Comparator.naturalOrder());
        
        // Verify products are sorted alphabetically A-Z
        assertEquals(expectedSortedNames, actualProductNames, 
                "Products should be sorted alphabetically A-Z");
    }
    
    /**
     * TC-006: Sort Products by Name Z-A
     * Steps:
     * 1. View products listing
     * 2. Select "Name (Z to A)" from sort dropdown
     * Expected Result: Products sorted alphabetically Z-A
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-006: Sort Products by Name Z-A")
    public void testSortProductsByNameZA() {
        // Sort products by name Z-A
        productsPage.sortProductsBy("Name (Z to A)");
        
        // Get the product names after sorting
        List<String> actualProductNames = productsPage.getProductNames();
        
        // Create a copy of the list and sort it in reverse order
        List<String> expectedSortedNames = new ArrayList<>(actualProductNames);
        expectedSortedNames.sort(Comparator.reverseOrder());
        
        // Verify products are sorted alphabetically Z-A
        assertEquals(expectedSortedNames, actualProductNames, 
                "Products should be sorted alphabetically Z-A");
    }
    
    /**
     * TC-007: Sort Products by Price Low-High
     * Steps:
     * 1. View products listing
     * 2. Select "Price (low to high)" from sort dropdown
     * Expected Result: Products sorted by price in ascending order
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-007: Sort Products by Price Low-High")
    public void testSortProductsByPriceLowHigh() {
        // Sort products by price low to high
        productsPage.sortProductsBy("Price (low to high)");
        
        // Get the product prices after sorting
        List<Double> actualPrices = productsPage.getProductPrices();
        
        // Create a copy of the list and sort it in ascending order
        List<Double> expectedSortedPrices = new ArrayList<>(actualPrices);
        expectedSortedPrices.sort(Comparator.naturalOrder());
        
        // Verify products are sorted by price in ascending order
        assertEquals(expectedSortedPrices, actualPrices, 
                "Products should be sorted by price in ascending order");
    }
    
    /**
     * TC-008: Sort Products by Price High-Low
     * Steps:
     * 1. View products listing
     * 2. Select "Price (high to low)" from sort dropdown
     * Expected Result: Products sorted by price in descending order
     * Priority: Medium
     */
    @Test
    @DisplayName("TC-008: Sort Products by Price High-Low")
    public void testSortProductsByPriceHighLow() {
        // Sort products by price high to low
        productsPage.sortProductsBy("Price (high to low)");
        
        // Get the product prices after sorting
        List<Double> actualPrices = productsPage.getProductPrices();
        
        // Create a copy of the list and sort it in descending order
        List<Double> expectedSortedPrices = new ArrayList<>(actualPrices);
        expectedSortedPrices.sort(Comparator.reverseOrder());
        
        // Verify products are sorted by price in descending order
        assertEquals(expectedSortedPrices, actualPrices, 
                "Products should be sorted by price in descending order");
    }
    
    /**
     * TC-009: View Product Details
     * Steps:
     * 1. View products listing
     * 2. Click on a product name
     * Expected Result: Product details page displayed with product information
     * Priority: High
     */
    @Test
    @DisplayName("TC-009: View Product Details")
    public void testViewProductDetails() {
        // Click on a product name to view its details
        ProductDetailsPage productDetailsPage = productsPage.clickOnProduct(TEST_PRODUCT);
        
        // Verify product details are displayed correctly
        assertEquals(TEST_PRODUCT, productDetailsPage.getProductName(), 
                "Product name should match the selected product");
        assertFalse(productDetailsPage.getProductDescription().isEmpty(), 
                "Product description should not be empty");
        assertTrue(productDetailsPage.getProductPrice() > 0, 
                "Product price should be greater than 0");
    }
    
    /**
     * TC-010: Add to Cart from Product Details
     * Steps:
     * 1. View product details page
     * 2. Click "Add to Cart" button
     * Expected Result: Product added to cart, cart counter updated
     * Priority: High
     */
    @Test
    @DisplayName("TC-010: Add to Cart from Product Details")
    public void testAddToCartFromProductDetails() {
        // Navigate to product details page
        ProductDetailsPage productDetailsPage = productsPage.clickOnProduct(TEST_PRODUCT);
        
        // Add the product to cart
        productDetailsPage.addToCart();
        
        // Verify cart counter is updated
        assertEquals(1, productDetailsPage.getCartCount(), 
                "Cart count should be 1 after adding product from details page");
        
        // Verify add to cart button changed to remove button
        assertTrue(productDetailsPage.isRemoveButtonDisplayed(), 
                "Remove button should be displayed after adding to cart");
    }
}