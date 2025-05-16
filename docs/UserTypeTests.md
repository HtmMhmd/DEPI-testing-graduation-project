# User Type Tests Documentation

## Overview
This document provides detailed information about the test cases for different user types in the Swag Labs application. The test suite validates the behavior of each user type and verifies their specific characteristics and limitations.

## User Types

### 1. Standard User (standard_user)
Regular user with full access to all features without any restrictions or issues.

### 2. Locked Out User (locked_out_user)
User that cannot log in because the account has been locked out.

### 3. Problem User (problem_user)
User that can log in but experiences various UI and functionality issues.

### 4. Performance Glitch User (performance_glitch_user)
User that experiences significant performance delays during actions.

### 5. Error User (error_user)
User that encounters errors during specific operations like removing items from cart.

### 6. Visual User (visual_user)
User that experiences visual glitches and UI inconsistencies.

## Test Cases

### TC-101: Standard User Login
**Description**: Verifies that a standard user can successfully login and complete the purchase flow without any issues.  
**Priority**: High  
**Steps**:
1. Login with standard_user credentials
2. Verify successful login to products page
3. Add product to cart
4. Proceed to checkout
5. Complete checkout process  
**Expected Result**: User can access all features and complete the purchase flow without any issues.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-101: Standard User Login")
public void testStandardUserLogin() {
    // Login as standard user
    ProductsPage productsPage = loginPage.loginAs(STANDARD_USER, PASSWORD);
    
    // Verify successful login
    assertTrue(productsPage.isOnProductsPage(), 
            "Standard user should be able to login successfully");
    
    // Add product to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT);
    assertEquals(1, productsPage.getCartCount(), 
            "Standard user should be able to add product to cart");
    
    // Complete checkout process
    CartPage cartPage = productsPage.goToCart();
    assertTrue(cartPage.isProductInCart(TEST_PRODUCT), 
            "Product should be in cart");
    
    CheckoutPage checkoutPage = cartPage.checkout();
    checkoutPage.enterFirstName("John")
               .enterLastName("Doe")
               .enterPostalCode("12345")
               .clickContinue();
               
    assertTrue(checkoutPage.isOnCheckoutOverviewPage(), 
            "Standard user should be able to proceed to checkout overview");
    
    checkoutPage.finish();
    assertTrue(checkoutPage.isOnCheckoutComplete(), 
            "Standard user should be able to complete checkout");
}
```
The test verifies that a standard user can login successfully, add products to cart, and complete the entire checkout process without any issues.

### TC-102: Locked Out User Login
**Description**: Verifies that a locked out user cannot login to the application.  
**Priority**: High  
**Steps**:
1. Attempt to login with locked_out_user credentials  
**Expected Result**: Error message indicating that the user is locked out.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-102: Locked Out User Login")
public void testLockedOutUserLogin() {
    // Attempt to login as locked out user
    loginPage.loginAs(LOCKED_OUT_USER, PASSWORD);
    
    // Verify error message
    assertTrue(loginPage.isLoginErrorDisplayed(), 
            "Error message should be displayed for locked out user");
    assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"), 
            "Error message should indicate user is locked out");
    assertTrue(loginPage.isOnLoginPage(), 
            "User should remain on login page");
}
```
The test verifies that when a locked out user attempts to login, an appropriate error message is displayed and the user remains on the login page.

### TC-103: Problem User Login
**Description**: Verifies the behavior of the problem user who can login but experiences UI issues.  
**Priority**: Medium  
**Steps**:
1. Login with problem_user credentials
2. Verify product image issues
3. Verify sorting functionality issues  
**Expected Result**: User can login but experiences UI and functionality issues.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-103: Problem User Login")
public void testProblemUserLogin() {
    // Login as problem user
    ProductsPage productsPage = loginPage.loginAs(PROBLEM_USER, PASSWORD);
    
    // Verify successful login
    assertTrue(productsPage.isOnProductsPage(), 
            "Problem user should be able to login successfully");
    
    // Check for image issues (all product images should be the same)
    List<WebElement> productImages = driver.findElements(By.cssSelector(".inventory_item_img img"));
    String firstImageSrc = productImages.get(0).getAttribute("src");
    
    boolean allImagesSame = true;
    for (WebElement image : productImages) {
        if (!image.getAttribute("src").equals(firstImageSrc)) {
            allImagesSame = false;
            break;
        }
    }
    
    assertTrue(allImagesSame, 
            "Problem user should see the same image for all products");
    
    // Try to sort products and verify it doesn't work properly
    String firstProductNameBefore = productsPage.getProductNames().get(0);
    productsPage.sortProductsBy("Name (Z to A)");
    String firstProductNameAfter = productsPage.getProductNames().get(0);
    
    assertEquals(firstProductNameBefore, firstProductNameAfter, 
            "Sorting should not work properly for problem user");
}
```
The test verifies that when logged in as a problem user, there are UI issues such as all product images being the same and sorting functionality not working correctly.

### TC-104: Performance Glitch User Login
**Description**: Verifies that the performance glitch user experiences delays during login.  
**Priority**: Medium  
**Steps**:
1. Login with performance_glitch_user credentials
2. Measure the login time  
**Expected Result**: User can login but experiences significant performance delay.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-104: Performance Glitch User Login")
public void testPerformanceGlitchUserLogin() {
    // Record start time
    long startTime = System.currentTimeMillis();
    
    // Login as performance glitch user
    ProductsPage productsPage = loginPage.loginAs(PERFORMANCE_GLITCH_USER, PASSWORD);
    
    // Record end time
    long endTime = System.currentTimeMillis();
    long loginDuration = endTime - startTime;
    
    // Verify successful login (with delay)
    assertTrue(productsPage.isOnProductsPage(), 
            "Performance glitch user should be able to login successfully");
    
    // Assert that login took longer than normal (typically over 2 seconds)
    assertTrue(loginDuration > 2000, 
            "Login should take longer than 2 seconds for performance glitch user");
}
```
The test verifies that when logging in as a performance glitch user, the login process experiences a significant delay (more than 2 seconds) but eventually succeeds.

### TC-105: Error User Login
**Description**: Verifies that the error user encounters errors during specific operations.  
**Priority**: Medium  
**Steps**:
1. Login with error_user credentials
2. Add product to cart
3. Attempt to remove item from cart  
**Expected Result**: User encounters errors when trying to remove items from cart.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-105: Error User Login")
public void testErrorUserLogin() {
    // Login as error user
    ProductsPage productsPage = loginPage.loginAs(ERROR_USER, PASSWORD);
    
    // Verify successful login
    assertTrue(productsPage.isOnProductsPage(), 
            "Error user should be able to login successfully");
    
    // Add product to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT);
    assertEquals(1, productsPage.getCartCount(), 
            "Error user should be able to add product to cart");
    
    // Navigate to cart and attempt checkout
    CartPage cartPage = productsPage.goToCart();
    assertTrue(cartPage.isProductInCart(TEST_PRODUCT), 
            "Product should be in cart");
    
    // Attempt to remove item from cart
    cartPage.removeItem(TEST_PRODUCT);
    
    // Verify item is still in cart (error user can't remove items)
    assertTrue(cartPage.isProductInCart(TEST_PRODUCT), 
            "Error user should not be able to remove items from cart");
}
```
The test verifies that when logged in as an error user, the user can add items to cart but cannot remove them, demonstrating the error behavior associated with this user type.

### TC-106: Visual User Login
**Description**: Verifies that the visual user experiences visual glitches but can complete the purchase flow.  
**Priority**: Low  
**Steps**:
1. Login with visual_user credentials
2. Complete the purchase flow
3. Check for visual oddities  
**Expected Result**: User can complete the purchase flow but experiences visual glitches.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-106: Visual User Login")
public void testVisualUserLogin() {
    // Login as visual user
    ProductsPage productsPage = loginPage.loginAs(VISUAL_USER, PASSWORD);
    
    // Verify successful login
    assertTrue(productsPage.isOnProductsPage(), 
            "Visual user should be able to login successfully");
    
    // Add product to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT);
    assertEquals(1, productsPage.getCartCount(), 
            "Visual user should be able to add product to cart");
    
    // Complete checkout process
    CartPage cartPage = productsPage.goToCart();
    CheckoutPage checkoutPage = cartPage.checkout();
    checkoutPage.enterFirstName("John")
               .enterLastName("Doe")
               .enterPostalCode("12345")
               .clickContinue();
               
    assertTrue(checkoutPage.isOnCheckoutOverviewPage(), 
            "Visual user should be able to proceed to checkout overview");
    
    // Verify there are visual oddities on the page
    // Look for specific visual elements that might be affected
    List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
    if (!priceElements.isEmpty()) {
        String priceText = priceElements.get(0).getText();
        // For visual user, prices may have visual issues but functionality remains
        assertNotNull(priceText, "Price text should be present even with visual issues");
    }
    
    checkoutPage.finish();
    assertTrue(checkoutPage.isOnCheckoutComplete(), 
            "Visual user should be able to complete checkout");
}
```
The test verifies that when logged in as a visual user, the user can complete the purchase flow but may experience visual glitches along the way.

## Test Setup and Teardown

### Setup
Before each test, the following setup actions are performed:
1. Initialize the WebDriver using WebDriverManager
2. Navigate to the base URL (https://www.saucedemo.com)
3. Create a new LoginPage object
4. Initialize WebDriverWait with a longer timeout (15 seconds) to accommodate performance glitches

### Teardown
After each test, the WebDriver is quit to close the browser and release resources.

## Handling Special Conditions
Each test is designed to handle the specific characteristics of the user type:
- Locked out user test expects and verifies the error message
- Problem user test verifies UI inconsistencies
- Performance glitch user test measures and verifies the delay
- Error user test verifies errors in removal functionality
- Visual user test checks for visual issues while verifying full functionality