package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductDetailsPage extends BasePage {
    // Multiple locators for product name to improve resilience
    @FindBy(css = ".inventory_details_name")
    private WebElement productName;
    
    // Alternative locator for product name
    private By productNameLocator = By.cssSelector(".inventory_details_name, .inventory_details_desc_container .inventory_item_name");
    
    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;
    
    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;
    
    @FindBy(css = "button[id^='add-to-cart']")
    private WebElement addToCartButton;
    
    @FindBy(css = "button[id^='remove']")
    private WebElement removeButton;
    
    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;
    
    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;
    
    // Constructor that waits for the page to be loaded
    public ProductDetailsPage() {
        super();
        waitForProductDetailsPageToLoad();
    }
    
    // Wait for product details page to load
    private void waitForProductDetailsPageToLoad() {
        try {
            // First wait for back button since it's a reliable indicator this is the details page
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-to-products")));
            
            // Then wait for product name using various possible locators
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_details_name")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item_name"))
            ));
            
        } catch (Exception e) {
            System.err.println("Error waiting for product details page to load: " + e.getMessage());
            // Take screenshot if possible
            try {
                if (driver != null) {
                    org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
                    byte[] screenshot = ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                    System.err.println("Screenshot taken of failed page load");
                }
            } catch (Exception screenshotError) {
                System.err.println("Could not take screenshot: " + screenshotError.getMessage());
            }
        }
    }
    
    public String getProductName() {
        try {
            return productName.getText();
        } catch (Exception e) {
            // Try alternative locator if the first one fails
            WebElement altProductName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productNameLocator));
            return altProductName.getText();
        }
    }
    
    public String getProductDescription() {
        try {
            return productDescription.getText();
        } catch (Exception e) {
            // Try a different approach if the first one fails
            WebElement altDescription = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".inventory_details_desc, .inventory_item_desc")));
            return altDescription.getText();
        }
    }
    
    public double getProductPrice() {
        try {
            String priceText = productPrice.getText().replace("$", "");
            return Double.parseDouble(priceText);
        } catch (Exception e) {
            // Try a different approach if the first one fails
            WebElement altPrice = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".inventory_details_price, .inventory_item_price")));
            String priceText = altPrice.getText().replace("$", "");
            return Double.parseDouble(priceText);
        }
    }
    
    public ProductDetailsPage addToCart() {
        addToCartButton.click();
        return this;
    }
    
    public ProductDetailsPage removeFromCart() {
        removeButton.click();
        return this;
    }
    
    public CartPage goToCart() {
        cartLink.click();
        return new CartPage();
    }
    
    public ProductsPage backToProducts() {
        backToProductsButton.click();
        return new ProductsPage();
    }
    
    public boolean isAddToCartButtonDisplayed() {
        try {
            return addToCartButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isRemoveButtonDisplayed() {
        try {
            return removeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getCartCount() {
        try {
            WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }
}