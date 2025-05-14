# Login Tests Documentation

## Overview
This document provides detailed information about the login test cases for the Swag Labs website. These tests verify the authentication functionality, including successful login, handling invalid credentials, and locked user accounts.

## Test Cases

### TC-001: Valid Login
**Description**: Verifies that a user can successfully log in with valid credentials.  
**Priority**: High  
**Steps**:
1. Navigate to login page
2. Enter valid username (`standard_user`) and password (`secret_sauce`)
3. Click Login button  
**Expected Result**: User is logged in and redirected to the products page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-001: Valid Login")
public void testValidLogin() {
    // Perform login with valid credentials
    ProductsPage productsPage = loginPage.loginAs(LoginPage.STANDARD_USER, LoginPage.PASSWORD);
    
    // Verify user is redirected to products page
    assertTrue(productsPage.isOnProductsPage(), 
               "User should be redirected to products page after successful login");
}
```
The test uses the `loginAs` method of the `LoginPage` class to perform the login operation with valid credentials, then verifies that the user is redirected to the Products page.

### TC-002: Invalid Login - Wrong Password
**Description**: Verifies that an error message is displayed when a user tries to log in with an incorrect password.  
**Priority**: High  
**Steps**:
1. Navigate to login page
2. Enter valid username but incorrect password
3. Click Login button  
**Expected Result**: Error message is displayed and user remains on the login page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-002: Invalid Login - Wrong Password")
public void testInvalidLoginWrongPassword() {
    // Perform login with invalid credentials (wrong password)
    loginPage.loginAs(LoginPage.STANDARD_USER, "wrong_password");
    
    // Verify error message is displayed and user remains on login page
    assertTrue(loginPage.isLoginErrorDisplayed(), 
               "Error message should be displayed for invalid password");
    assertTrue(loginPage.isOnLoginPage(), 
               "User should remain on login page after failed login");
}
```
The test attempts to log in with a valid username but incorrect password, then verifies that an error message is displayed and the user remains on the login page.

### TC-003: Invalid Login - Empty Fields
**Description**: Verifies that validation messages are displayed when a user tries to log in with empty fields.  
**Priority**: Medium  
**Steps**:
1. Navigate to login page
2. Leave username and password fields empty
3. Click Login button  
**Expected Result**: Validation message displayed and user remains on the login page.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-003: Invalid Login - Empty Fields")
public void testInvalidLoginEmptyFields() {
    // Click login button without entering credentials
    loginPage.clickLoginButton();
    
    // Verify error message is displayed and user remains on login page
    assertTrue(loginPage.isLoginErrorDisplayed(), 
               "Error message should be displayed for empty fields");
    assertTrue(loginPage.isOnLoginPage(), 
               "User should remain on login page when fields are empty");
}
```
The test clicks the login button without entering any credentials, then verifies that an error message is displayed and the user remains on the login page.

### TC-027: Login with Locked User
**Description**: Verifies that an appropriate error message is displayed when a locked-out user tries to log in.  
**Priority**: Medium  
**Steps**:
1. Navigate to login page
2. Enter locked user credentials
3. Click Login button  
**Expected Result**: Appropriate error message displayed indicating that the user is locked out.  
**Code Explanation**:
```java
@Test
@DisplayName("TC-027: Login with Locked User")
public void testLoginWithLockedUser() {
    // Perform login with locked out user
    loginPage.loginAs(LoginPage.LOCKED_OUT_USER, LoginPage.PASSWORD);
    
    // Verify error message is displayed and contains "locked out" text
    assertTrue(loginPage.isLoginErrorDisplayed(), 
               "Error message should be displayed for locked user");
    assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"), 
               "Error message should indicate that user is locked out");
}
```
The test attempts to log in with a locked-out user account and verifies that an appropriate error message is displayed indicating that the user is locked out.

## Implementation Details

### Page Object: LoginPage
The `LoginPage` class is responsible for interacting with the login page elements. It provides methods for entering credentials, clicking the login button, and checking for error messages.

**Key Methods**:
- `enterUsername(String username)`: Enters the username in the username field
- `enterPassword(String password)`: Enters the password in the password field
- `clickLoginButton()`: Clicks the login button and returns a ProductsPage object
- `loginAs(String username, String password)`: Combines the above methods for a complete login flow
- `getErrorMessage()`: Gets the text of the error message
- `isLoginErrorDisplayed()`: Checks if an error message is displayed
- `isOnLoginPage()`: Verifies if the user is on the login page

### Test Setup and Teardown
Each test follows the same setup and teardown process:

**Setup**:
- Initialize the WebDriver using WebDriverManager
- Navigate to the base URL (https://www.saucedemo.com)
- Create a new LoginPage object

**Teardown**:
- Quit the WebDriver to close the browser and release resources
