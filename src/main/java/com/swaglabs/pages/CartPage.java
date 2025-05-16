package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {
    // Locators
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;
    
    // Multiple ways to locate checkout button for resilience
    @FindBy(id = "checkout")
    private WebElement checkoutButton;
    
    @FindBy(className = "cart_quantity")
    private List<WebElement> itemQuantities;
    
    // Methods
    public boolean isOnCartPage() {
        return pageTitle.isDisplayed() && pageTitle.getText().equals("Your Cart");
    }
    
    public int getNumberOfItemsInCart() {
        return cartItems.size();
    }
    
    public boolean isProductInCart(String productName) {
        return cartItems.stream()
                .anyMatch(item -> item.findElement(By.className("inventory_item_name")).getText().equals(productName));
    }
    
    public CartPage removeItem(String productName) {
        String xpath = String.format("//div[text()='%s']/ancestor::div[@class='cart_item']//button", productName);
        click(By.xpath(xpath));
        return this;
    }
    
    public ProductsPage continueShopping() {
        continueShoppingButton.click();
        return new ProductsPage();
    }
    
    public CheckoutPage checkout() {
        try {
            // Wait for the cart page to be fully loaded
            wait.until(driver -> isOnCartPage());
            
            // Wait for checkout button with ID
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout")));
            
            // Try finding checkout button with multiple selectors if needed
            WebElement button;
            try {
                // Try ID first (most reliable)
                button = driver.findElement(By.id("checkout"));
            } catch (Exception e) {
                // Fallback to data-test attribute
                System.out.println("Couldn't find checkout button by ID, trying data-test attribute");
                button = driver.findElement(By.cssSelector("[data-test='checkout']"));
            }
            
            // Ensure the button is visible and clickable
            wait.until(ExpectedConditions.elementToBeClickable(button));
            
            // Scroll the button into view before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            
            // First try normal click
            button.click();
            
            // Wait for checkout page to load (indicating successful click)
            wait.until(driver -> driver.findElement(By.className("checkout_info")).isDisplayed());
            
        } catch (Exception e) {
            // If normal click fails, try JavaScript click as a fallback
            System.out.println("Standard checkout button click failed: " + e.getMessage());
            System.out.println("Attempting JavaScript click as fallback");
            
            try {
                WebElement button = driver.findElement(By.cssSelector("#checkout, [data-test='checkout'], .checkout_button"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            } catch (Exception jsException) {
                System.out.println("JavaScript click also failed: " + jsException.getMessage());
                throw new RuntimeException("Failed to click checkout button after multiple attempts", jsException);
            }
        }
        
        return new CheckoutPage();
    }
    
    public List<String> getCartItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.className("inventory_item_name")).getText())
                .collect(Collectors.toList());
    }
    
    public double getTotalPrice() {
        return cartItems.stream()
                .mapToDouble(item -> {
                    String priceText = item.findElement(By.className("inventory_item_price")).getText();
                    return Double.parseDouble(priceText.replace("$", ""));
                })
                .sum();
    }
}