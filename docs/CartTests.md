# Cart Tests Documentation

## Overview
This document provides detailed information about the cart functionality test cases for the Swag Labs website. These tests verify the functionality related to adding items to cart, removing items, cart counter updates, and cart persistence during navigation.

## Test Cases

### TC-011: Add Product to Cart
**Description**: Verifies that a product can be added to the cart from the products listing page.  
**Priority**: High  
**Steps**:
1. View product listing
2. Click "Add to Cart" button for a product  
**Expected Result**: Product is added to cart and cart counter is updated.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-011: Add Product to Cart")
public void testAddProductToCart() {
    // Add product to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    
    // Verify cart counter is updated
    assertEquals(1, productsPage.getCartCount(), 
                "Cart count should be 1 after adding product");
    
    // Navigate to cart page and verify product is in cart
    CartPage cartPage = productsPage.goToCart();
    assertTrue(cartPage.isProductInCart(TEST_PRODUCT1), 
                "Product should be in cart after clicking Add to Cart button");
}
```
The test adds a product to the cart, verifies that the cart counter is updated to 1, and then checks that the product appears in the cart page.

### TC-012: Add Multiple Products to Cart
**Description**: Verifies that multiple products can be added to the cart and the cart counter shows the correct count.  
**Priority**: High  
**Steps**:
1. View product listing
2. Click "Add to Cart" for multiple products  
**Expected Result**: All products are added to cart and cart counter shows the correct count.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-012: Add Multiple Products to Cart")
public void testAddMultipleProductsToCart() {
    // Add multiple products to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1)
               .clickAddToCartForProduct(TEST_PRODUCT2);
    
    // Verify cart counter is updated
    assertEquals(2, productsPage.getCartCount(), 
                "Cart count should be 2 after adding two products");
    
    // Navigate to cart page and verify both products are in cart
    CartPage cartPage = productsPage.goToCart();
    List<String> cartItems = cartPage.getCartItemNames();
    
    assertTrue(cartItems.contains(TEST_PRODUCT1), "First product should be in cart");
    assertTrue(cartItems.contains(TEST_PRODUCT2), "Second product should be in cart");
    assertEquals(2, cartItems.size(), "Cart should contain exactly 2 items");
}
```
The test adds two different products to the cart, verifies that the cart counter shows 2, and then checks that both products appear in the cart page.

### TC-013: Remove Product from Cart
**Description**: Verifies that a product can be removed from the cart using the "Remove" button on the cart page.  
**Priority**: High  
**Steps**:
1. Add product to cart
2. Go to cart page
3. Click "Remove" button for a product  
**Expected Result**: Product is removed from the cart.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-013: Remove Product from Cart")
public void testRemoveProductFromCart() {
    // Add product to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    
    // Go to cart page
    CartPage cartPage = productsPage.goToCart();
    
    // Verify product is in cart
    assertTrue(cartPage.isProductInCart(TEST_PRODUCT1), 
               "Product should be in cart initially");
    
    // Remove product from cart
    cartPage.removeItem(TEST_PRODUCT1);
    
    // Verify product is removed
    assertEquals(0, cartPage.getNumberOfItemsInCart(), 
                "Cart should be empty after removing the product");
}
```
The test adds a product to the cart, navigates to the cart page, verifies that the product is there, removes the product, and then checks that the cart is empty.

### TC-014: Update Cart from Product Page
**Description**: Verifies that a product can be removed from the cart using the "Remove" button on the product listing page.  
**Priority**: Medium  
**Steps**:
1. Add product to cart
2. Click "Remove" button on product page  
**Expected Result**: Product is removed from the cart.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-014: Update Cart from Product Page")
public void testUpdateCartFromProductPage() {
    // Add product to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    
    // Verify cart counter is updated
    assertEquals(1, productsPage.getCartCount(), 
                "Cart count should be 1 after adding product");
    
    // Remove product from product listing page
    productsPage.clickRemoveForProduct(TEST_PRODUCT1);
    
    // Verify cart counter is updated
    assertEquals(0, productsPage.getCartCount(), 
                "Cart count should be 0 after removing product");
    
    // Navigate to cart page and verify it's empty
    CartPage cartPage = productsPage.goToCart();
    assertEquals(0, cartPage.getNumberOfItemsInCart(), "Cart should be empty");
}
```
The test adds a product to the cart, verifies that the cart counter shows 1, removes the product using the "Remove" button on the product listing page, verifies that the cart counter is updated to 0, and then checks that the cart page is empty.

### TC-015: Continue Shopping
**Description**: Verifies that clicking the "Continue Shopping" button on the cart page redirects back to the products page.  
**Priority**: Medium  
**Steps**:
1. Add products to cart
2. Navigate to cart page
3. Click "Continue Shopping" button  
**Expected Result**: User is redirected back to the products page.  
**Code Explanation**:
```java
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
```
The test adds a product to the cart, navigates to the cart page, clicks the "Continue Shopping" button, and verifies that the user is redirected back to the products page.

### TC-016: Cart Persistence
**Description**: Verifies that cart contents remain unchanged after navigating away from the cart page and back.  
**Priority**: Medium  
**Steps**:
1. Add products to cart
2. Navigate to product details page
3. Return to products page  
**Expected Result**: Cart contents remain unchanged.  
**Code Explanation**:
```java
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
```
The test adds a product to the cart, records the cart count, navigates to a different product's details page and then back to the products page, and verifies that the cart count remains unchanged.

## Implementation Details

### Page Object: CartPage
The `CartPage` class is responsible for interacting with the cart page elements. It provides methods for checking items in the cart, removing items, and navigating to other pages.

**Key Methods**:
- `isOnCartPage()`: Verifies if the user is on the cart page
- `getNumberOfItemsInCart()`: Returns the number of items in the cart
- `isProductInCart(String productName)`: Checks if a specific product is in the cart
- `removeItem(String productName)`: Removes an item from the cart
- `continueShopping()`: Clicks the "Continue Shopping" button and returns a ProductsPage object
- `getCartItemNames()`: Returns a list of product names in the cart

### Page Object: ProductsPage
The `ProductsPage` class is responsible for interacting with the products listing page elements. It provides methods for adding products to the cart, removing products, and checking the cart count.

**Key Methods**:
- `clickAddToCartForProduct(String productName)`: Adds a product to the cart
- `clickRemoveForProduct(String productName)`: Removes a product from the cart
- `getCartCount()`: Returns the number of items in the cart
- `goToCart()`: Navigates to the cart page and returns a CartPage object

### Test Setup and Teardown
Each test follows the same setup and teardown process:

**Setup**:
- Initialize the WebDriver using WebDriverManager
- Navigate to the base URL (https://www.saucedemo.com)
- Log in with standard user credentials
- Create a new ProductsPage object

**Teardown**:
- Quit the WebDriver to close the browser and release resources
