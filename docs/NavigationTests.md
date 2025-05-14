# Navigation Tests Documentation

## Overview
This document provides detailed information about the navigation test cases for the Swag Labs website. These tests verify the functionality related to the hamburger menu, navigation between pages, and overall site navigation behavior.

## Test Cases

### TC-025: Open Menu
**Description**: Verifies that clicking the hamburger menu button opens the navigation menu.  
**Priority**: Medium  
**Steps**:
1. Log in to the application
2. Click hamburger menu button  
**Expected Result**: Navigation menu is displayed.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-025: Open Menu")
public void testOpenMenu() {
    // Click hamburger menu button
    productsPage.openMenu();
    
    // Verify navigation menu is displayed
    assertTrue(productsPage.isMenuVisible(), 
               "Navigation menu should be visible after clicking hamburger button");
}
```
The test clicks the hamburger menu button and verifies that the navigation menu is displayed.

### TC-026: Close Menu
**Description**: Verifies that clicking the close button (X) in the navigation menu closes the menu.  
**Priority**: Medium  
**Steps**:
1. Open the navigation menu
2. Click close (X) button  
**Expected Result**: Navigation menu is closed.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-026: Close Menu")
public void testCloseMenu() {
    // Open the menu
    productsPage.openMenu();
    
    // Verify menu is visible
    assertTrue(productsPage.isMenuVisible(), 
               "Navigation menu should be visible initially");
    
    // Close the menu
    productsPage.closeMenu();
    
    // Verify menu is no longer visible
    assertFalse(productsPage.isMenuVisible(), 
                "Navigation menu should be hidden after clicking close button");
}
```
The test opens the navigation menu, verifies that it is visible, clicks the close button, and verifies that the menu is no longer visible.

### TC-027: Log Out
**Description**: Verifies that clicking the "Logout" option in the navigation menu logs out the user.  
**Priority**: High  
**Steps**:
1. Open the navigation menu
2. Click "Logout" option  
**Expected Result**: User is logged out and redirected to the login page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-027: Log Out")
public void testLogOut() {
    // Open the menu and wait for it to appear
    productsPage.openMenu();
    productsPage.waitForMenuToAppear();
    
    // Click logout and verify redirection to login page
    LoginPage loginPage = productsPage.logout();
    
    // Verify user is redirected to login page
    assertTrue(loginPage.isOnLoginPage(), 
               "User should be redirected to login page after logout");
}
```
The test opens the navigation menu, waits for it to appear, clicks the "Logout" option, and verifies that the user is redirected to the login page.

### TC-028: Invalid URL Access
**Description**: Verifies that the application redirects users to the login page when they attempt to access protected pages without authentication.  
**Priority**: Medium  
**Steps**:
1. Log out of the application
2. Attempt to directly access a protected page URL  
**Expected Result**: User is redirected to login page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-028: Invalid URL Access")
public void testInvalidUrlAccess() {
    // Logout first - make sure we open the menu first and wait for it to appear
    productsPage.openMenu();
    productsPage.waitForMenuToAppear();
    
    try {
        // Click the logout link
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
        logoutLink.click();
    } catch (Exception e) {
        // Handle the exception by directly navigating to the login page as fallback
        driver.get("https://www.saucedemo.com/");
    }
    
    // Try to access inventory page directly
    driver.get("https://www.saucedemo.com/inventory.html");
    
    // Wait for redirection and verify login page is displayed
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    
    // Verify user is redirected to login page
    assertTrue(driver.findElement(By.id("login-button")).isDisplayed(),
           "Login button should be visible when trying to access protected pages without login");
}
```
The test first logs out of the application, then attempts to directly access the inventory page URL. It verifies that the user is redirected to the login page by checking for the presence of the login button.

### TC-029: About Page Navigation
**Description**: Verifies that clicking the "About" option in the navigation menu redirects to the Sauce Labs website.  
**Priority**: Low  
**Steps**:
1. Open the navigation menu
2. Click "About" option  
**Expected Result**: User is redirected to the Sauce Labs website.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-029: About Page Navigation")
public void testAboutPageNavigation() {
    // Open the menu and click about
    productsPage.openMenu()
               .clickAbout();
    
    // Give page time to load
    wait.until(ExpectedConditions.urlContains("saucelabs.com"));
    
    // Verify user is redirected to Sauce Labs website
    assertTrue(driver.getCurrentUrl().contains("saucelabs.com"), 
               "User should be redirected to Sauce Labs website");
}
```
The test opens the navigation menu, clicks the "About" option, and verifies that the user is redirected to the Sauce Labs website.

### TC-030: All Items Navigation
**Description**: Verifies that clicking the "All Items" option in the navigation menu redirects to the products page.  
**Priority**: Medium  
**Steps**:
1. Navigate to a different page (e.g., cart page)
2. Open the navigation menu
3. Click "All Items" option  
**Expected Result**: User is redirected to the products page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-030: All Items Navigation")
public void testAllItemsNavigation() {
    // First navigate to cart page
    CartPage cartPage = productsPage.goToCart();
    
    // Open the menu and click all items
    ProductsPage redirectedPage = cartPage.openMenu()
                                         .clickAllItems();
    
    // Verify user is redirected to products page
    assertTrue(redirectedPage.isOnProductsPage(), 
               "User should be redirected to products page after clicking All Items");
}
```
The test navigates to the cart page, opens the navigation menu, clicks the "All Items" option, and verifies that the user is redirected to the products page.

### TC-031: Footer Social Media Links
**Description**: Verifies that social media links in the footer are working correctly.  
**Priority**: Low  
**Steps**:
1. Identify social media links in the footer (Twitter, Facebook, LinkedIn)
2. Verify links have correct URLs  
**Expected Result**: Social media links have correct target URLs.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-031: Footer Social Media Links")
public void testFooterSocialMediaLinks() {
    // Get social media links
    Map<String, String> socialMediaLinks = productsPage.getSocialMediaLinks();
    
    // Verify Twitter link
    assertTrue(socialMediaLinks.containsKey("Twitter"), "Twitter link should exist");
    assertTrue(socialMediaLinks.get("Twitter").contains("twitter.com"), 
               "Twitter link should point to twitter.com");
    
    // Verify Facebook link
    assertTrue(socialMediaLinks.containsKey("Facebook"), "Facebook link should exist");
    assertTrue(socialMediaLinks.get("Facebook").contains("facebook.com"), 
               "Facebook link should point to facebook.com");
    
    // Verify LinkedIn link
    assertTrue(socialMediaLinks.containsKey("LinkedIn"), "LinkedIn link should exist");
    assertTrue(socialMediaLinks.get("LinkedIn").contains("linkedin.com"), 
               "LinkedIn link should point to linkedin.com");
}
```
The test retrieves the social media links from the footer and verifies that they point to the correct URLs.

### TC-032: Browser Navigation
**Description**: Verifies that browser back and forward buttons work correctly within the application.  
**Priority**: Medium  
**Steps**:
1. Navigate through multiple pages
2. Use browser back button
3. Use browser forward button  
**Expected Result**: Navigation behaves as expected, maintaining state.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-032: Browser Navigation")
public void testBrowserNavigation() {
    // Add item to cart and navigate to cart
    productsPage.clickAddToCartForProduct(TEST_PRODUCT1);
    CartPage cartPage = productsPage.goToCart();
    
    // Verify we're on cart page
    assertTrue(cartPage.isOnCartPage(), "Should be on cart page initially");
    
    // Navigate back using browser button
    driver.navigate().back();
    
    // Verify we're back on products page
    assertTrue(new ProductsPage(driver).isOnProductsPage(), 
               "Should navigate back to products page");
    
    // Navigate forward using browser button
    driver.navigate().forward();
    
    // Verify we're back on cart page
    assertTrue(new CartPage(driver).isOnCartPage(), 
               "Should navigate forward to cart page");
}
```
The test navigates from the products page to the cart page, uses the browser back button, verifies that the user is back on the products page, uses the browser forward button, and verifies that the user is back on the cart page.

## Implementation Details

### Page Object: BasePage
The `BasePage` class is the parent class for all page objects and provides common methods for navigation and menu interaction.

**Key Methods**:
- `openMenu()`: Clicks the hamburger menu button to open the navigation menu
- `closeMenu()`: Clicks the close button to close the navigation menu
- `isMenuVisible()`: Checks if the navigation menu is visible
- `clickLogout()`: Clicks the logout option in the navigation menu
- `clickAllItems()`: Clicks the all items option in the navigation menu
- `clickAbout()`: Clicks the about option in the navigation menu
- `getSocialMediaLinks()`: Returns a map of social media links in the footer

### Required Updates for Improved Navigation Testing
To ensure robust navigation testing, we made several important updates:

1. Added explicit waits for the menu items to appear:
```java
public BasePage openMenu() {
    wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
    waitForMenuToAppear();
    return this;
}

public void waitForMenuToAppear() {
    wait.until(ExpectedConditions.visibilityOf(menuContainer));
}
```

2. Improved error handling with fallback approaches:
```java
public ProductsPage clickAllItems() {
    try {
        wait.until(ExpectedConditions.elementToBeClickable(allItemsLink)).click();
        return new ProductsPage(driver);
    } catch (TimeoutException e) {
        System.out.println("Timeout waiting for All Items link, trying JavaScript click");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", allItemsLink);
        return new ProductsPage(driver);
    }
}
```

### Test Setup and Teardown
Each test follows the same setup and teardown process:

**Setup**:
- Initialize the WebDriver using WebDriverManager
- Initialize WebDriverWait for explicit waits
- Navigate to the base URL (https://www.saucedemo.com)
- Log in with standard user credentials
- Create a new ProductsPage object

**Teardown**:
- Quit the WebDriver to close the browser and release resources
