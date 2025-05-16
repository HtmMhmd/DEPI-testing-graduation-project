package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.swaglabs.utils.WebDriverManager; // Add import for WebDriverManager

public class ProductDetailsPage extends BasePage {
    // Multiple locators for product name to improve resilience
    @FindBy(css = ".inventory_details_name")
    private WebElement productName;
    
    // Additional locators with broader patterns
    private By productNameLocator = By.cssSelector(
        ".inventory_details_name, .inventory_details_desc_container .inventory_item_name, .inventory_item_name");
    
    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;
    
    // Additional description locator
    private By productDescriptionLocator = By.cssSelector(
        ".inventory_details_desc, .inventory_item_desc");
    
    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;
    
    // Additional price locator
    private By productPriceLocator = By.cssSelector(
        ".inventory_details_price, .inventory_item_price");
    
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
    
    // Wait for product details page to load with improved detection
    private void waitForProductDetailsPageToLoad() {
        try {
            // First wait for URL to contain an indicator of product details page
            wait.until(driver -> 
                driver.getCurrentUrl().contains("inventory-item.html") || 
                driver.getCurrentUrl().contains("item.html"));
            
            // Wait for back button since it's a reliable indicator this is the details page
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.id("back-to-products")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".back-button"))
                ));
            } catch (Exception e) {
                System.out.println("Back button not found, continuing: " + e.getMessage());
            }
            
            // Try multiple approaches to find product details
            try {
                // Wait for any product detail element to be visible using a variety of possible selectors
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_details_name")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item_name")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_details_container")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_details_desc"))
                ));
            } catch (Exception e) {
                System.out.println("Product details elements not found with initial selectors: " + e.getMessage());
                
                // Last resort - wait for any content in the main container
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#inventory_item_container")));
            }
            
            // Use JavaScript to check if page is fully loaded
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("return document.readyState").equals("complete");
            
        } catch (Exception e) {
            System.err.println("Error waiting for product details page to load: " + e.getMessage());
            // Take screenshot for debugging
            WebDriverManager.captureScreenshot("ProductDetailsPageLoadError");
        }
    }
    
    public String getProductName() {
        try {
            // Try multiple approaches to get product name
            // First try the @FindBy element
            if (productName != null && isElementDisplayed(productName)) {
                return productName.getText();
            }
            
            // Second, try by locator
            WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productNameLocator));
            return element.getText();
        } catch (Exception e) {
            // Third, try any element that might contain the product name
            try {
                // Get text from inventory container as last resort
                WebElement container = driver.findElement(By.cssSelector("#inventory_item_container"));
                String containerText = container.getText();
                // Try to extract product name from text content if possible
                String[] lines = containerText.split("\\n");
                if (lines.length > 0) {
                    return lines[0]; // First line might be the title
                }
                return "Product Name Not Found";
            } catch (Exception ex) {
                System.err.println("Failed to get product name: " + ex.getMessage());
                return "Product Name Not Available";
            }
        }
    }
    
    public String getProductDescription() {
        try {
            if (productDescription != null && isElementDisplayed(productDescription)) {
                return productDescription.getText();
            }
            
            WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productDescriptionLocator));
            return element.getText();
        } catch (Exception e) {
            System.err.println("Failed to get product description: " + e.getMessage());
            return "Description Not Available";
        }
    }
    
    public double getProductPrice() {
        try {
            if (productPrice != null && isElementDisplayed(productPrice)) {
                String priceText = productPrice.getText().replace("$", "");
                return Double.parseDouble(priceText);
            }
            
            WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productPriceLocator));
            String priceText = element.getText().replace("$", "");
            return Double.parseDouble(priceText);
        } catch (Exception e) {
            System.err.println("Failed to get product price: " + e.getMessage());
            return 0.0;
        }
    }
    
    // Helper method to safely check if element is displayed
    private boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
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