package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.swaglabs.enums.UserType;

public class LoginPage extends BasePage {
    // Locators
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    private final By errorMessageLocator = By.cssSelector("[data-test='error']");

    // All user types
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_OUT_USER = "locked_out_user";
    public static final String PROBLEM_USER = "problem_user";
    public static final String PERFORMANCE_GLITCH_USER = "performance_glitch_user";
    public static final String ERROR_USER = "error_user";
    public static final String VISUAL_USER = "visual_user";
    public static final String PASSWORD = "secret_sauce";

    // Methods
    public void enterUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public ProductsPage clickLoginButton() {
        loginButton.click();
        return new ProductsPage();
    }

    public ProductsPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    public String getErrorMessage() {
        return getText(errorMessageLocator);
    }

    public boolean isLoginErrorDisplayed() {
        return isElementDisplayed(errorMessageLocator);
    }

    public boolean isOnLoginPage() {
        return loginButton.isDisplayed();
    }
    
    /**
     * Login with a specific user type
     * @param userType The type of user to login as
     * @return The Products page if login is successful
     */
    public ProductsPage login(UserType userType) {
        return loginAs(userType.getUsername(), userType.getPassword());
    }
    
    /**
     * Check if error message is displayed
     * @return true if error message is displayed
     */
    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorMessageLocator);
    }
}
