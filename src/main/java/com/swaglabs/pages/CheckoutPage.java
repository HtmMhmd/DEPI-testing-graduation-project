package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {
    // Locators
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(id = "first-name")
    private WebElement firstNameField;
    
    @FindBy(id = "last-name")
    private WebElement lastNameField;
    
    @FindBy(id = "postal-code")
    private WebElement zipCodeField;
    
    @FindBy(id = "continue")
    private WebElement continueButton;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    // Checkout Step Two locators
    @FindBy(className = "summary_info")
    private WebElement orderSummary;
    
    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;
    
    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;
    
    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;
    
    @FindBy(id = "finish")
    private WebElement finishButton;
    
    // Checkout Complete locators
    @FindBy(className = "complete-header")
    private WebElement completeHeader;
      // Using a more flexible locator for the back home button that matches different possible IDs
    @FindBy(css = "#back-to-products, #back-home, .checkout_complete_container .btn")
    private WebElement backHomeButton;
    
    // Methods for Checkout Step One
    public boolean isOnCheckoutStepOne() {
        return pageTitle.isDisplayed() && pageTitle.getText().equals("Checkout: Your Information");
    }
    
    public CheckoutPage enterFirstName(String firstName) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
        return this;
    }
    
    public CheckoutPage enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
        return this;
    }
    
    public CheckoutPage enterZipCode(String zipCode) {
        zipCodeField.clear();
        zipCodeField.sendKeys(zipCode);
        return this;
    }
    
    public CheckoutPage fillCustomerInfo(String firstName, String lastName, String zipCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterZipCode(zipCode);
        return this;
    }
      public CheckoutPage clickContinue() {
        continueButton.click();
        // Add explicit wait for the checkout overview page to load
        try {
            wait.until(condition -> pageTitle.isDisplayed() && 
                                   pageTitle.getText().equals("Checkout: Overview"));
        } catch (Exception e) {
            // If timeout occurs, we'll handle it in the test assertion
        }
        return this;
    }
    
    public CartPage clickCancel() {
        cancelButton.click();
        return new CartPage();
    }
    
    public String getErrorMessage() {
        return errorMessage.getText();
    }
    
    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Methods for Checkout Step Two
    public boolean isOnCheckoutStepTwo() {
        return pageTitle.isDisplayed() && pageTitle.getText().equals("Checkout: Overview");
    }
    
    public double getSubtotalAmount() {
        String subtotal = subtotalLabel.getText().replace("Item total: $", "");
        return Double.parseDouble(subtotal);
    }
    
    public double getTaxAmount() {
        String tax = taxLabel.getText().replace("Tax: $", "");
        return Double.parseDouble(tax);
    }
    
    public double getTotalAmount() {
        String total = totalLabel.getText().replace("Total: $", "");
        return Double.parseDouble(total);
    }
    
    public CheckoutPage clickFinish() {
        finishButton.click();
        return this;
    }
    
    // Methods for Checkout Complete
    public boolean isOnCheckoutComplete() {
        try {
            wait.until(driver -> {
                try {
                    return completeHeader.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
            
            return completeHeader.isDisplayed() && 
                  (completeHeader.getText().toLowerCase().contains("thank you") || 
                   completeHeader.getText().toLowerCase().contains("complete") ||
                   completeHeader.getText().toLowerCase().contains("order"));
        } catch (Exception e) {
            // If we can't find the complete header, look for any indication we're on the complete page
            try {
                WebElement anyCompletePageElement = driver.findElement(
                    By.cssSelector(".checkout_complete_container, .complete-text, [data-test='complete-text']"));
                return anyCompletePageElement.isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }
      public ProductsPage clickBackHome() {
        // Wait for the back home button to be clickable before attempting to click
        try {
            wait.until(driver -> {
                try {
                    return backHomeButton.isDisplayed() && backHomeButton.isEnabled();
                } catch (Exception e) {
                    return false;
                }
            });
            backHomeButton.click();
        } catch (Exception e) {
            System.out.println("Error clicking back home button: " + e.getMessage());
            // Try an alternative approach - find by any available identifier
            try {
                // Look for any button that might be the "back to products" button
                WebElement alternativeButton = driver.findElement(
                    By.cssSelector(".checkout_complete_container button, #back-to-products, [data-test='back-to-products']"));
                alternativeButton.click();
            } catch (Exception ex) {
                throw new RuntimeException("Could not find back home button: " + ex.getMessage());
            }
        }
        return new ProductsPage();
    }
}