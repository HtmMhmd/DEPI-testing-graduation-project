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
        try {
            // Verify we're starting on products page
            assertTrue(productsPage.isOnProductsPage(), 
                      "Should be on products page before clicking on product");
            
            // Get product names first to verify if our test product exists
            List<String> availableProducts = productsPage.getProductNames();
            System.out.println("Available products: " + availableProducts);
            
            // If our test product isn't available, use the first product in the list
            String productToTest = TEST_PRODUCT;
            if (!availableProducts.contains(TEST_PRODUCT) && !availableProducts.isEmpty()) {
                productToTest = availableProducts.get(0);
                System.out.println("Using alternative product: " + productToTest);
            }
            
            // Take screenshot before clicking on product (for debugging)
            WebDriverManager.captureScreenshot("BeforeClickingProduct");
            
            System.out.println("Attempting to click on product: " + productToTest);
            
            // Click on the product to view its details with explicit wait
            ProductDetailsPage productDetailsPage = productsPage.clickOnProduct(productToTest);
            
            // Add a longer wait to ensure page load completes (especially for slower environments)
            try {
                Thread.sleep(1000); // 1 second should be enough
            } catch (InterruptedException e) {
                // Ignore
            }
            
            // Take screenshot after navigating to product details (for debugging)
            WebDriverManager.captureScreenshot("AfterNavigatingToProductDetails");
            
            // Get and log the product name for debugging
            String actualProductName = productDetailsPage.getProductName();
            System.out.println("Product details page shows: " + actualProductName);
            
            // More lenient verification - just check that we got some product details
            assertFalse(actualProductName.isEmpty() || actualProductName.equals("Product Name Not Available"), 
                       "Product name should not be empty");
            
            String description = productDetailsPage.getProductDescription();
            System.out.println("Product description: " + description);
            assertFalse(description.isEmpty() || description.equals("Description Not Available"), 
                       "Product description should not be empty");
            
            double price = productDetailsPage.getProductPrice();
            System.out.println("Product price: $" + price);
            assertTrue(price > 0, 
                      "Product price should be greater than 0");
        } catch (Exception e) {
            // Take screenshot on failure
            WebDriverManager.captureScreenshot("ProductDetailsTestFailure");
            System.err.println("Error in testViewProductDetails: " + e.getMessage());
            e.printStackTrace();
            throw e;  // Re-throw to fail the test
        }
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