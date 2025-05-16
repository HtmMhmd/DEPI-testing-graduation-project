package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
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
            // Wait up to 20 seconds for the cart page to be fully loaded and stable
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(driver -> isOnCartPage());
            
            // Try multiple selector strategies to find the checkout button
            WebElement button = null;
            List<String> selectorStrategies = Arrays.asList(
                "id:checkout", 
                "css:.checkout_button", 
                "css:[data-test='checkout']", 
                "xpath://button[contains(text(),'Checkout')]"
            );
            
            // Try each selector strategy until we find the button
            for (String strategy : selectorStrategies) {
                try {
                    String[] parts = strategy.split(":", 2);
                    String type = parts[0];
                    String selector = parts[1];
                    
                    switch (type) {
                        case "id":
                            button = driver.findElement(By.id(selector));
                            break;
                        case "css":
                            button = driver.findElement(By.cssSelector(selector));
                            break;
                        case "xpath":
                            button = driver.findElement(By.xpath(selector));
                            break;
                    }
                    
                    if (button != null && button.isDisplayed()) {
                        System.out.println("Found checkout button using: " + strategy);
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Could not find button with " + strategy + ": " + e.getMessage());
                }
            }
            
            if (button == null) {
                throw new RuntimeException("Checkout button not found with any selector strategy");
            }
            
            // Scroll the button into view before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            
            // Wait for button to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(button));
            
            // Take screenshot before clicking
            WebDriverManager.captureScreenshot("BeforeCheckoutClick");
            
            // Try normal click first
            button.click();
            System.out.println("Clicked checkout button with normal click");
            
            // Wait for checkout page to load with timeout
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("checkout_info")));
                System.out.println("Navigation to checkout page successful");
            } catch (Exception e) {
                System.out.println("Normal click might have failed, trying JavaScript click: " + e.getMessage());
                
                // Try JavaScript click as fallback
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                System.out.println("Clicked checkout button with JavaScript click");
                
                // Wait again for checkout page
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("checkout_info")));
            }
            
        } catch (Exception e) {
            WebDriverManager.captureScreenshot("CheckoutFailure");
            System.err.println("Critical failure in checkout process: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to click checkout button after multiple attempts", e);
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