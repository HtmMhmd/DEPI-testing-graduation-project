# User Type Tests Documentation

## Overview
This document provides detailed information about the user type test cases for the Swag Labs website. These tests verify the functionality and behavior specific to different user types (standard_user, locked_out_user, problem_user, etc.).

## User Types in Swag Labs

Swag Labs offers several user types with different behaviors to help test various scenarios:

| User Type | Username | Description |
|-----------|----------|-------------|
| Standard User | standard_user | Normal user with full functionality |
| Locked Out User | locked_out_user | User that cannot log in |
| Problem User | problem_user | User that experiences UI/UX issues |
| Performance Glitch User | performance_glitch_user | User that experiences slow performance |
| Error User | error_user | User that encounters errors in functionality |
| Visual User | visual_user | User that sees visual glitches in the UI |

## Test Cases

### TC-101: Standard User Login
**Description**: Verifies that the standard user can log in and access all features normally.  
**Priority**: High  
**Steps**:
1. Navigate to login page
2. Login with standard_user credentials
3. Attempt to add product to cart and proceed to checkout  
**Expected Result**: User can log in successfully, add products to cart, and complete checkout.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-101: Standard User Login")
public void testStandardUser() {
    // Login as standard user
    ProductsPage productsPage = loginPage.loginAs(UserType.STANDARD_USER.getUsername(), 
                                                 LoginPage.PASSWORD);
    
    // Verify user is on products page
    assertTrue(productsPage.isOnProductsPage(), 
              "Standard user should be able to login successfully");
    
    // Add product to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT);
    assertEquals(1, productsPage.getCartCount(), 
                "Standard user should be able to add product to cart");
    
    // Complete a simple checkout process
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    
    // Fill checkout info and verify checkout steps work
    checkoutPage.fillCustomerInfo("Standard", "User", "12345");
    checkoutPage.clickContinue();
    
    // Verify on checkout step two
    assertTrue(checkoutPage.isOnCheckoutStepTwo(), 
              "Standard user should be able to complete checkout");
}
```
The test logs in with standard_user credentials, verifies that login is successful, adds a product to the cart, and completes the checkout process.

### TC-102: Locked Out User Login
**Description**: Verifies that the locked-out user cannot log in and sees an appropriate error message.  
**Priority**: High  
**Steps**:
1. Navigate to login page
2. Login with locked_out_user credentials  
**Expected Result**: Login fails with a message indicating the user is locked out.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-102: Locked Out User Login")
public void testLockedOutUser() {
    // Try to login as locked out user
    loginPage.loginAs(UserType.LOCKED_OUT_USER.getUsername(), LoginPage.PASSWORD);
    
    // Verify error message is displayed
    assertTrue(loginPage.isLoginErrorDisplayed(), 
              "Locked out user should see an error message");
    
    // Verify error message contains "locked out" text
    String errorMessage = loginPage.getErrorMessage().toLowerCase();
    assertTrue(errorMessage.contains("locked out"), 
              "Error should indicate that user is locked out");
}
```
The test attempts to log in with locked_out_user credentials and verifies that an appropriate error message is displayed.

### TC-103: Problem User Login
**Description**: Verifies that the problem user can log in but experiences UI/UX issues.  
**Priority**: Medium  
**Steps**:
1. Navigate to login page
2. Login with problem_user credentials
3. Check for UI inconsistencies  
**Expected Result**: User can log in successfully but experiences UI issues like incorrect product images and non-functional sorting.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-103: Problem User Login")
public void testProblemUser() {
    // Login as problem user
    ProductsPage productsPage = loginPage.loginAs(UserType.PROBLEM_USER.getUsername(), 
                                                 LoginPage.PASSWORD);
    
    // Verify user is on products page
    assertTrue(productsPage.isOnProductsPage(), 
              "Problem user should be able to login successfully");
    
    // Check for UI inconsistencies - all product images should be the same
    List<WebElement> productImages = driver.findElements(By.className("inventory_item_img"));
    String firstImageSrc = productImages.get(0).findElement(By.tagName("img")).getAttribute("src");
    
    // Check if all images have the same source (problem_user bug)
    boolean allImagesSame = true;
    for (WebElement imageContainer : productImages) {
        WebElement image = imageContainer.findElement(By.tagName("img"));
        if (!image.getAttribute("src").equals(firstImageSrc)) {
            allImagesSame = false;
            break;
        }
    }
    
    assertTrue(allImagesSame, 
              "Problem user should see the same image for all products");
    
    // Try sorting - another known issue for problem_user
    productsPage.sortProductsBy("Name (Z to A)");
    List<String> productNames = productsPage.getProductNames();
    
    // For problem_user, sorting doesn't work properly
    assertFalse(productNames.get(0).compareTo(productNames.get(productNames.size() - 1)) > 0,
               "Problem user's sorting functionality should not work properly");
}
```
The test logs in with problem_user credentials, verifies that login is successful, and checks for known UI issues such as all product images being the same and sorting functionality not working as expected.

### TC-104: Performance Glitch User Login
**Description**: Verifies that the performance glitch user experiences a delay during login.  
**Priority**: Medium  
**Steps**:
1. Navigate to login page
2. Login with performance_glitch_user credentials
3. Measure login time  
**Expected Result**: User can log in successfully but experiences a performance delay during login.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-104: Performance Glitch User Login")
public void testPerformanceGlitchUser() {
    // Record start time
    long startTime = System.currentTimeMillis();
    
    // Login as performance glitch user
    ProductsPage productsPage = loginPage.loginAs(UserType.PERFORMANCE_GLITCH_USER.getUsername(), 
                                                 LoginPage.PASSWORD);
    
    // Record end time
    long endTime = System.currentTimeMillis();
    long loginTime = endTime - startTime;
    
    // Verify user is on products page
    assertTrue(productsPage.isOnProductsPage(), 
              "Performance glitch user should be able to login successfully");
    
    // Verify login took longer than normal (typically > 2 seconds)
    System.out.println("Performance glitch user login time: " + loginTime + " ms");
    assertTrue(loginTime > 2000, 
              "Performance glitch user should experience a delay during login");
}
```
The test measures the time it takes to log in with performance_glitch_user credentials and verifies that the login takes longer than normal (typically more than 2 seconds).

### TC-105: Error User Login
**Description**: Verifies that the error user can log in but experiences functionality issues.  
**Priority**: Medium  
**Steps**:
1. Navigate to login page
2. Login with error_user credentials
3. Attempt to add products to cart  
**Expected Result**: User can log in successfully but experiences errors with cart functionality.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-105: Error User Login")
public void testErrorUser() {
    // Login as error user
    ProductsPage productsPage = loginPage.loginAs(UserType.ERROR_USER.getUsername(), 
                                                 LoginPage.PASSWORD);
    
    // Verify user is on products page
    assertTrue(productsPage.isOnProductsPage(), 
              "Error user should be able to login successfully");
    
    // Try adding product to cart - Known issue: cart icon doesn't update or shows inconsistent behavior
    productsPage.clickAddToCartForProduct(TEST_PRODUCT);
    
    // Go to cart and check if product was added correctly
    CartPage cartPage = productsPage.goToCart();
    
    // The error_user should have issues with cart functionality
    // This test may need to be adjusted based on the exact behavior of error_user
    if (!cartPage.isProductInCart(TEST_PRODUCT)) {
        System.out.println("Error user cart functionality issue detected: Product not in cart as expected");
    }
}
```
The test logs in with error_user credentials, verifies that login is successful, and checks for expected functionality issues like problems with the cart system.

### TC-106: Visual User Login
**Description**: Verifies that the visual user can log in but may see visual inconsistencies.  
**Priority**: Low  
**Steps**:
1. Navigate to login page
2. Login with visual_user credentials  
**Expected Result**: User can log in successfully but may experience visual glitches in the UI.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-106: Visual User Login")
public void testVisualUser() {
    // Login as visual user
    ProductsPage productsPage = loginPage.loginAs(UserType.VISUAL_USER.getUsername(), 
                                                 LoginPage.PASSWORD);
    
    // Verify user is on products page
    assertTrue(productsPage.isOnProductsPage(), 
              "Visual user should be able to login successfully");
    
    // Visual user should be able to add products to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT);
    assertEquals(1, productsPage.getCartCount(), 
                "Visual user should be able to add product to cart");
    
    // Note: Visual inconsistencies are difficult to test programmatically
    // In a real project, we might use image comparison tools or visual testing frameworks
    System.out.println("Visual user test complete - manual verification of visual elements recommended");
}
```
The test logs in with visual_user credentials and verifies that basic functionality works. It acknowledges that visual inconsistencies are difficult to test programmatically and suggests manual verification.

## Implementation Details

### Page Object: LoginPage
The `LoginPage` class is responsible for handling login functionality. It provides methods for entering credentials, checking for error messages, and handling user type-specific login behavior.

**Key Methods**:
- `loginAs(String username, String password)`: Logs in with the specified credentials
- `login(UserType userType)`: Logs in with the credentials for the specified user type
- `isLoginErrorDisplayed()`: Checks if a login error message is displayed
- `getErrorMessage()`: Gets the text of the login error message

### UserType Enum
The `UserType` enum defines the available user types in Swag Labs and provides methods for accessing their credentials.

**User Types**:
- `STANDARD_USER`: Normal user with full functionality
- `LOCKED_OUT_USER`: User that cannot log in
- `PROBLEM_USER`: User that experiences UI/UX issues
- `PERFORMANCE_GLITCH_USER`: User that experiences slow performance
- `ERROR_USER`: User that encounters errors in functionality
- `VISUAL_USER`: User that sees visual glitches in the UI

**Key Methods**:
- `getUsername()`: Gets the username for the user type
- `getPassword()`: Gets the password for the user type (same for all users)

## Test Setup and Teardown
Each test follows the same setup and teardown process:

**Setup**:
- Initialize the WebDriver using WebDriverManager
- Navigate to the base URL (https://www.saucedemo.com)
- Initialize WebDriverWait for explicit waits
- Create a new LoginPage object

**Teardown**:
- Quit the WebDriver to close the browser and release resources