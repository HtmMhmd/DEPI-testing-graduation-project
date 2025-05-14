package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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
        checkoutButton.click();
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