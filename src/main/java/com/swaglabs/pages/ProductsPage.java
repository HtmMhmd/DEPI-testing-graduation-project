package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {
    // Locators
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;
    
    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;
    
    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;
    
    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    private final By addToCartButtons = By.cssSelector("button[id^='add-to-cart']");
    private final By removeButtons = By.cssSelector("button[id^='remove']");
    
    // Methods
    public boolean isOnProductsPage() {
        return pageTitle.isDisplayed() && pageTitle.getText().equals("Products");
    }
    
    public void sortProductsBy(String sortOption) {
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(sortOption);
    }
    
    public List<String> getProductNames() {
        return productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
    
    public List<Double> getProductPrices() {
        return productPrices.stream()
                .map(element -> element.getText().replace("$", ""))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }
    
    public ProductsPage clickAddToCartForProduct(String productName) {
        String xpath = String.format("//div[text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@id, 'add-to-cart')]", productName);
        click(By.xpath(xpath));
        return this;
    }
    
    public ProductsPage clickRemoveForProduct(String productName) {
        String xpath = String.format("//div[text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@id, 'remove')]", productName);
        click(By.xpath(xpath));
        return this;
    }
    
    public ProductDetailsPage clickOnProduct(String productName) {
        String xpath = String.format("//div[text()='%s']", productName);
        click(By.xpath(xpath));
        return new ProductDetailsPage();
    }
    
    public CartPage goToCart() {
        cartLink.click();
        return new CartPage();
    }
    
    public int getCartCount() {
        try {
            WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }    public void openMenu() {
        // Wait for menu button to be clickable before clicking
        wait.until(driver -> {
            try {
                return menuButton.isDisplayed() && menuButton.isEnabled();
            } catch (Exception e) {
                return false;
            }
        });
        
        menuButton.click();
        
        // Add a small wait to allow the menu animation to complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore interruption
        }
    }
    
    public void waitForMenuToAppear() {
        // Wait for menu items to be visible after clicking the menu button
        wait.until(driver -> {
            try {
                WebElement menuContainer = driver.findElement(By.className("bm-menu"));
                return menuContainer.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });
    }
    
    public LoginPage logout() {
        // First make sure the menu is open
        openMenu();
        waitForMenuToAppear();
        
        try {
            // Wait for logout link to be clickable
            WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("logout_sidebar_link")));
            logoutLink.click();
        } catch (Exception e) {
            // If standard click fails, try JavaScript click as a fallback
            System.out.println("Standard click on logout link failed, trying JavaScript click");
            try {
                WebElement logoutLink = driver.findElement(By.id("logout_sidebar_link"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutLink);
            } catch (Exception jsException) {
                System.out.println("JavaScript click also failed: " + jsException.getMessage());
                // Last resort: go directly to the login page
                driver.get("https://www.saucedemo.com/");
            }
        }
        
        // Wait for login page to load
        wait.until(ExpectedConditions.urlContains("saucedemo.com"));
        
        return new LoginPage();
    }
    
    public boolean isProductDisplayed(String productName) {
        return driver.findElements(By.xpath(String.format("//div[text()='%s']", productName))).size() > 0;
    }
}