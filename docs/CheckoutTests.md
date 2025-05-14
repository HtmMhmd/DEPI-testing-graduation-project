# Checkout Tests Documentation

## Overview
This document provides detailed information about the checkout process test cases for the Swag Labs website. These tests verify the functionality related to the complete checkout flow, including information form submission, order completion, and error handling during checkout.

## Test Cases

### TC-017: Start Checkout Process
**Description**: Verifies that clicking the "Checkout" button on the cart page redirects to the checkout information page.  
**Priority**: High  
**Steps**:
1. Add product to cart
2. Navigate to cart page
3. Click "Checkout" button  
**Expected Result**: User is redirected to the checkout information page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-017: Start Checkout Process")
public void testStartCheckoutProcess() {
    // Add product to cart and navigate to cart page
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    
    // Click checkout button
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Verify user is on checkout information page
    assertTrue(checkoutPage.isOnCheckoutInfoPage(), 
               "User should be redirected to checkout information page");
}
```
The test adds a product to the cart, navigates to the cart page, clicks the "Checkout" button, and verifies that the user is redirected to the checkout information page.

### TC-018: Submit Checkout Information
**Description**: Verifies that valid checkout information can be submitted successfully.  
**Priority**: High  
**Steps**:
1. Navigate to checkout information page
2. Fill in first name, last name, and postal code
3. Click "Continue" button  
**Expected Result**: User is redirected to the checkout overview page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-018: Submit Checkout Information")
public void testSubmitCheckoutInformation() {
    // Add product to cart, navigate to cart page, and checkout
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Fill checkout information and continue
    checkoutPage.enterFirstName("John")
                .enterLastName("Doe")
                .enterPostalCode("12345")
                .clickContinue();
    
    // Verify user is on checkout overview page
    assertTrue(checkoutPage.isOnCheckoutOverviewPage(), 
               "User should be redirected to checkout overview page");
}
```
The test navigates to the checkout information page, fills in the required information, clicks the "Continue" button, and verifies that the user is redirected to the checkout overview page.

### TC-019: Checkout Information Validation - Empty Fields
**Description**: Verifies that validation messages are displayed when checkout information fields are left empty.  
**Priority**: Medium  
**Steps**:
1. Navigate to checkout information page
2. Leave all fields empty
3. Click "Continue" button  
**Expected Result**: Validation error message displayed.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-019: Checkout Information Validation - Empty Fields")
public void testCheckoutInformationValidation() {
    // Add product to cart, navigate to cart page, and checkout
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Click continue without entering any information
    checkoutPage.clickContinue();
    
    // Verify error message is displayed
    assertTrue(checkoutPage.isErrorDisplayed(), 
               "Error message should be displayed for empty fields");
    assertTrue(checkoutPage.getErrorMessage().contains("First Name"), 
               "Error message should mention the required field");
}
```
The test navigates to the checkout information page, clicks the "Continue" button without filling in any information, and verifies that an error message is displayed.

### TC-020: Cancel Checkout Information
**Description**: Verifies that clicking the "Cancel" button on the checkout information page redirects back to the cart page.  
**Priority**: Medium  
**Steps**:
1. Navigate to checkout information page
2. Click "Cancel" button  
**Expected Result**: User is redirected back to the cart page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-020: Cancel Checkout Information")
public void testCancelCheckoutInformation() {
    // Add product to cart, navigate to cart page, and checkout
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Click cancel button
    CartPage redirectedPage = checkoutPage.cancel();
    
    // Verify user is redirected back to cart page
    assertTrue(redirectedPage.isOnCartPage(), 
               "User should be redirected back to cart page after canceling checkout");
}
```
The test navigates to the checkout information page, clicks the "Cancel" button, and verifies that the user is redirected back to the cart page.

### TC-021: Verify Checkout Overview
**Description**: Verifies that the checkout overview page displays correct product information, subtotal, tax, and total.  
**Priority**: High  
**Steps**:
1. Add product to cart
2. Complete checkout information form
3. View checkout overview page  
**Expected Result**: Checkout overview page displays correct product information, subtotal, tax, and total.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-021: Verify Checkout Overview")
public void testVerifyCheckoutOverview() {
    // Add product to cart, navigate to cart page, and checkout
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Fill checkout information and continue
    checkoutPage.enterFirstName("John")
                .enterLastName("Doe")
                .enterPostalCode("12345")
                .clickContinue();
    
    // Verify checkout overview information
    assertTrue(checkoutPage.isProductInOverview(TEST_PRODUCT1), 
               "Product should appear in the checkout overview");
    
    double itemTotal = checkoutPage.getItemTotal();
    double tax = checkoutPage.getTax();
    double total = checkoutPage.getTotal();
    
    assertTrue(itemTotal > 0, "Item total should be greater than 0");
    assertTrue(tax > 0, "Tax should be greater than 0");
    assertEquals(itemTotal + tax, total, 0.01, 
                "Total should equal item total plus tax");
}
```
The test navigates to the checkout overview page and verifies that the product information, subtotal, tax, and total are displayed correctly.

### TC-022: Cancel Checkout Overview
**Description**: Verifies that clicking the "Cancel" button on the checkout overview page redirects back to the products page.  
**Priority**: Medium  
**Steps**:
1. Navigate to checkout overview page
2. Click "Cancel" button  
**Expected Result**: User is redirected to the products page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-022: Cancel Checkout Overview")
public void testCancelCheckoutOverview() {
    // Add product to cart, navigate to cart page, and checkout
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Fill checkout information and continue to overview page
    checkoutPage.enterFirstName("John")
                .enterLastName("Doe")
                .enterPostalCode("12345")
                .clickContinue();
    
    // Click cancel button
    ProductsPage redirectedPage = checkoutPage.cancelOverview();
    
    // Verify user is redirected to products page
    assertTrue(redirectedPage.isOnProductsPage(), 
               "User should be redirected to products page after canceling checkout overview");
}
```
The test navigates to the checkout overview page, clicks the "Cancel" button, and verifies that the user is redirected to the products page.

### TC-023: Complete Checkout
**Description**: Verifies that clicking the "Finish" button on the checkout overview page completes the order and displays the checkout complete page.  
**Priority**: High  
**Steps**:
1. Navigate to checkout overview page
2. Click "Finish" button  
**Expected Result**: Order is completed, and user is redirected to checkout complete page with a success message.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-023: Complete Checkout")
public void testCompleteCheckout() {
    // Add product to cart, navigate to cart page, and checkout
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Fill checkout information and continue to overview page
    checkoutPage.enterFirstName("John")
                .enterLastName("Doe")
                .enterPostalCode("12345")
                .clickContinue();
    
    // Complete checkout
    checkoutPage.finish();
    
    // Verify user is on checkout complete page with success message
    assertTrue(checkoutPage.isOnCheckoutComplete(), 
               "User should be redirected to checkout complete page");
    assertTrue(checkoutPage.getCompleteHeaderText().contains("THANK YOU"),
               "Checkout complete page should display a thank you message");
}
```
The test navigates through the checkout process, clicks the "Finish" button on the overview page, and verifies that the user is redirected to the checkout complete page with a success message.

### TC-024: Back to Home from Checkout Complete
**Description**: Verifies that clicking the "Back Home" button on the checkout complete page redirects to the products page.  
**Priority**: Medium  
**Steps**:
1. Complete checkout process
2. Click "Back Home" button on checkout complete page  
**Expected Result**: User is redirected to the products page and cart is empty.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-024: Back to Home from Checkout Complete")
public void testBackToHomeFromCheckoutComplete() {
    // Add product to cart, navigate to cart page, and checkout
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Complete checkout process
    checkoutPage.enterFirstName("John")
                .enterLastName("Doe")
                .enterPostalCode("12345")
                .clickContinue()
                .finish();
    
    // Click back home button
    ProductsPage redirectedPage = checkoutPage.clickBackHome();
    
    // Verify user is redirected to products page with empty cart
    assertTrue(redirectedPage.isOnProductsPage(), 
               "User should be redirected to products page after clicking Back Home");
    assertEquals(0, redirectedPage.getCartCount(), 
                "Cart should be empty after completing checkout");
}
```
The test completes the checkout process, clicks the "Back Home" button on the checkout complete page, and verifies that the user is redirected to the products page with an empty cart.

## Implementation Details

### Page Object: CheckoutPage
The `CheckoutPage` class is responsible for interacting with the checkout pages (information, overview, and complete). It provides methods for entering checkout information, verifying overview details, and completing the checkout process.

**Key Methods**:
- `isOnCheckoutInfoPage()`: Verifies if the user is on the checkout information page
- `enterFirstName(String firstName)`: Enters the first name in the checkout information form
- `enterLastName(String lastName)`: Enters the last name in the checkout information form
- `enterPostalCode(String postalCode)`: Enters the postal code in the checkout information form
- `clickContinue()`: Clicks the "Continue" button on the checkout information page
- `cancel()`: Clicks the "Cancel" button on the checkout information page
- `isErrorDisplayed()`: Checks if an error message is displayed
- `getErrorMessage()`: Returns the error message text
- `isOnCheckoutOverviewPage()`: Verifies if the user is on the checkout overview page
- `isProductInOverview(String productName)`: Checks if a specific product is in the checkout overview
- `getItemTotal()`: Returns the item total amount
- `getTax()`: Returns the tax amount
- `getTotal()`: Returns the total amount
- `finish()`: Clicks the "Finish" button on the checkout overview page
- `cancelOverview()`: Clicks the "Cancel" button on the checkout overview page
- `isOnCheckoutComplete()`: Verifies if the user is on the checkout complete page
- `getCompleteHeaderText()`: Returns the header text on the checkout complete page
- `clickBackHome()`: Clicks the "Back Home" button on the checkout complete page

### Test Setup and Teardown
Each test follows the same setup and teardown process:

**Setup**:
- Initialize the WebDriver using WebDriverManager
- Navigate to the base URL (https://www.saucedemo.com)
- Log in with standard user credentials
- Create a new ProductsPage object

**Teardown**:
- Quit the WebDriver to close the browser and release resources
