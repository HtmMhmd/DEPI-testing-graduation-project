package com.swaglabs.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

/**
 * JUnit 5 TestWatcher Extension to capture screenshots when tests fail
 */
public class TestListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // Use ThreadLocal WebDriver Manager to get current thread's driver
        WebDriver driver = ThreadLocalWebDriverManager.getDriver();
        if (driver != null) {
            // Capture and save screenshot
            var screenshotFile = ThreadLocalWebDriverManager.captureScreenshot(context.getDisplayName());
            
            // Log failure info with more details
            System.err.println("TEST FAILED: " + context.getDisplayName());
            System.err.println("  Error: " + cause.getMessage());
            if (screenshotFile != null) {
                System.err.println("  Screenshot saved: " + screenshotFile.getPath());
            }
        }
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        WebDriver driver = ThreadLocalWebDriverManager.getDriver();
        if (driver != null) {
            var screenshotFile = ThreadLocalWebDriverManager.captureScreenshot(context.getDisplayName() + "-aborted");
            System.err.println("TEST ABORTED: " + context.getDisplayName() + 
                    " - Reason: " + cause.getMessage());
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        // You could also capture screenshots for successful tests if needed
        System.out.println("TEST PASSED: " + context.getDisplayName());
    }
}
