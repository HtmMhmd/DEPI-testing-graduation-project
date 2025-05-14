# Product Tests Documentation

## Overview
This document provides detailed information about the product browsing and filtering test cases for the Swag Labs website. These tests verify the functionality related to viewing products, sorting them, examining product details, and adding products to the cart from the product details page.

## Test Cases

### TC-004: View Products List
**Description**: Verifies that the products page displays a list of products after login.  
**Priority**: High  
**Steps**:
1. Login to the application  
**Expected Result**: Products page displayed with a list of products.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-004: View Products List")
public void testViewProductsList() {
    // Verify products page is displayed after login
    assertTrue(productsPage.isOnProductsPage(), 
               "Products page should be displayed after login");
    
    // Verify product list is not empty
    List<String> productNames = productsPage.getProductNames();
    assertFalse(productNames.isEmpty(), 
                "Product list should not be empty");
}
```
The test verifies that the products page is displayed after logging in and that the product list contains items.

### TC-005: Sort Products by Name A-Z
**Description**: Verifies that products can be sorted alphabetically from A to Z.  
**Priority**: Medium  
**Steps**:
1. View products listing
2. Select "Name (A to Z)" from sort dropdown  
**Expected Result**: Products sorted alphabetically A-Z.  
**Code Explanation**:
```java
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
```
The test sorts products by name in ascending order (A-Z) and verifies that the order matches what would be expected from a proper alphabetical sort.

### TC-006: Sort Products by Name Z-A
**Description**: Verifies that products can be sorted alphabetically from Z to A.  
**Priority**: Medium  
**Steps**:
1. View products listing
2. Select "Name (Z to A)" from sort dropdown  
**Expected Result**: Products sorted alphabetically Z-A.  
**Code Explanation**:
```java
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
```
The test sorts products by name in descending order (Z-A) and verifies that the order matches what would be expected from a proper reverse alphabetical sort.

### TC-007: Sort Products by Price Low-High
**Description**: Verifies that products can be sorted by price in ascending order.  
**Priority**: Medium  
**Steps**:
1. View products listing
2. Select "Price (low to high)" from sort dropdown  
**Expected Result**: Products sorted by price in ascending order.  
**Code Explanation**:
```java
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
```
The test sorts products by price in ascending order and verifies that the prices are correctly ordered from lowest to highest.

### TC-008: Sort Products by Price High-Low
**Description**: Verifies that products can be sorted by price in descending order.  
**Priority**: Medium  
**Steps**:
1. View products listing
2. Select "Price (high to low)" from sort dropdown  
**Expected Result**: Products sorted by price in descending order.  
**Code Explanation**:
```java
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
```
The test sorts products by price in descending order and verifies that the prices are correctly ordered from highest to lowest.

### TC-009: View Product Details
**Description**: Verifies that clicking on a product name displays the product details page with correct information.  
**Priority**: High  
**Steps**:
1. View products listing
2. Click on a product name  
**Expected Result**: Product details page displayed with product information.  
**Code Explanation**:
```java
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
```
The test clicks on a specific product name and verifies that the product details page shows the correct product name, has a non-empty description, and displays a valid price.

### TC-010: Add to Cart from Product Details
**Description**: Verifies that a product can be added to the cart from the product details page.  
**Priority**: High  
**Steps**:
1. View product details page
2. Click "Add to Cart" button  
**Expected Result**: Product is added to the cart, and the cart counter is updated.  
**Code Explanation**:
```java
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
```
The test navigates to a product details page, adds the product to the cart, and verifies that the cart counter is updated and the "Add to Cart" button has changed to a "Remove" button.

## Implementation Details

### Page Object: ProductsPage
The `ProductsPage` class is responsible for interacting with the products listing page elements. It provides methods for sorting products, getting product information, and clicking on products.

**Key Methods**:
- `isOnProductsPage()`: Verifies if the user is on the products page
- `sortProductsBy(String sortOption)`: Selects a sorting option from the dropdown
- `getProductNames()`: Returns a list of product names
- `getProductPrices()`: Returns a list of product prices
- `clickOnProduct(String productName)`: Clicks on a product name and returns a ProductDetailsPage object

### Page Object: ProductDetailsPage
The `ProductDetailsPage` class is responsible for interacting with the product details page elements. It provides methods for getting product information and adding products to the cart.

**Key Methods**:
- `getProductName()`: Returns the name of the product
- `getProductDescription()`: Returns the description of the product
- `getProductPrice()`: Returns the price of the product
- `addToCart()`: Clicks the "Add to Cart" button
- `getCartCount()`: Returns the number of items in the cart
- `isRemoveButtonDisplayed()`: Checks if the "Remove" button is displayed

### Test Setup and Teardown
Each test follows the same setup and teardown process:

**Setup**:
- Initialize the WebDriver using WebDriverManager
- Navigate to the base URL (https://www.saucedemo.com)
- Log in with standard user credentials
- Create a new ProductsPage object

**Teardown**:
- Quit the WebDriver to close the browser and release resources
