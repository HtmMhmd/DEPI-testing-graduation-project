package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductDetailsPage extends BasePage {
    // Locators
    @FindBy(css = ".inventory_details_name")
    private WebElement productName;
    
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
    
    // Methods
    public String getProductName() {
        return productName.getText();
    }
    
    public String getProductDescription() {
        return productDescription.getText();
    }
    
    public double getProductPrice() {
        return Double.parseDouble(productPrice.getText().replace("$", ""));
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