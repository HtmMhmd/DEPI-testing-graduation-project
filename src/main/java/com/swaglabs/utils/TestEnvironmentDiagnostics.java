package com.swaglabs.utils;

import org.openqa.selenium.WebDriver;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Diagnostic utility to check environment setup before running tests
 */
public class TestEnvironmentDiagnostics {

    private static final String[] URLS_TO_CHECK = {
        "https://www.saucedemo.com",
        "https://www.google.com"
    };

    /**
     * Main method to run diagnostics
     */
    public static void main(String[] args) {
        System.out.println("=== Swag Labs Test Environment Diagnostics ===");
        
        checkJavaVersion();
        checkOperatingSystem();
        checkNetworkConnectivity();
        checkWebDriverSetup();
        checkScreenshotDirectory();
        checkBrowserLaunch();
        
        System.out.println("\n=== Diagnostics Complete ===");
    }
    
    /**
     * Check Java version
     */
    private static void checkJavaVersion() {
        System.out.println("\n[Checking Java Version]");
        String javaVersion = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        System.out.println("Java Version: " + javaVersion);
        System.out.println("Java Vendor: " + javaVendor);
        
        try {
            double version = Double.parseDouble(
                javaVersion.substring(0, javaVersion.indexOf(".", javaVersion.indexOf(".") + 1))
            );
            System.out.println("Java Version Check: " + (version >= 11 ? "PASS" : "FAIL (Requires Java 11+)"));
        } catch (Exception e) {
            System.out.println("Java Version Check: UNKNOWN (Could not parse version number)");
        }
    }
    
    /**
     * Check operating system
     */
    private static void checkOperatingSystem() {
        System.out.println("\n[Checking Operating System]");
        String os = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");
        
        System.out.println("Operating System: " + os);
        System.out.println("Version: " + osVersion);
        System.out.println("Architecture: " + osArch);
    }
    
    /**
     * Check network connectivity to required sites
     */
    private static void checkNetworkConnectivity() {
        System.out.println("\n[Checking Network Connectivity]");
        
        for (String urlString : URLS_TO_CHECK) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(5000);
                int responseCode = connection.getResponseCode();
                
                System.out.println(urlString + ": " + 
                    (responseCode >= 200 && responseCode < 400 ? 
                     "PASS (Response Code: " + responseCode + ")" : 
                     "FAIL (Response Code: " + responseCode + ")"));
                
            } catch (Exception e) {
                System.out.println(urlString + ": FAIL (Error: " + e.getMessage() + ")");
            }
        }
    }
    
    /**
     * Check WebDriver setup
     */    private static void checkWebDriverSetup() {
        System.out.println("\n[Checking WebDriver Setup]");
        
        // Check Chrome setup
        try {
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            System.out.println("Chrome WebDriver Setup: PASS");
            
            String chromePath = getBrowserPath("chrome");
            if (chromePath != null) {
                System.out.println("Chrome Found: " + chromePath);
            } else {
                System.out.println("Chrome Not Found in common locations. Test execution with Chrome may fail.");
            }
        } catch (Exception e) {
            System.out.println("Chrome WebDriver Setup: FAIL (" + e.getMessage() + ")");
        }
        
        // Check Firefox setup
        try {
            io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
            System.out.println("Firefox WebDriver Setup: PASS");
            
            String firefoxPath = getBrowserPath("firefox");
            if (firefoxPath != null) {
                System.out.println("Firefox Found: " + firefoxPath);
            } else {
                System.out.println("Firefox Not Found in common locations. Test execution with Firefox may fail.");
            }
        } catch (Exception e) {
            System.out.println("Firefox WebDriver Setup: FAIL (" + e.getMessage() + ")");
        }
        
        // Check Edge setup
        try {
            io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
            System.out.println("Edge WebDriver Setup: PASS");
            
            String edgePath = getBrowserPath("edge");
            if (edgePath != null) {
                System.out.println("Edge Found: " + edgePath);
            } else {
                System.out.println("Edge Not Found in common locations. Test execution with Edge may fail.");
            }
        } catch (Exception e) {
            System.out.println("Edge WebDriver Setup: FAIL (" + e.getMessage() + ")");
        }
    }
    
    /**
     * Check if the screenshots directory can be created/written
     */
    private static void checkScreenshotDirectory() {
        System.out.println("\n[Checking Screenshot Directory]");
        File screenshotsDir = new File("test-screenshots");
        
        if (!screenshotsDir.exists()) {
            boolean created = screenshotsDir.mkdirs();
            System.out.println("Creating directory: " + (created ? "PASS" : "FAIL"));
        } else {
            System.out.println("Directory exists: PASS");
        }
        
        boolean canWrite = screenshotsDir.canWrite();
        System.out.println("Directory writable: " + (canWrite ? "PASS" : "FAIL"));
    }
      /**
     * Check if browser can be launched
     */
    private static void checkBrowserLaunch() {
        System.out.println("\n[Checking Browser Launch]");
        
        // Get currently configured browser type
        String configuredBrowser = System.getProperty("browser", "chrome");
        System.out.println("Currently configured browser: " + configuredBrowser);
        
        // Try to launch the configured browser
        checkSpecificBrowserLaunch(configuredBrowser);
    }
    
    /**
     * Check if a specific browser can be launched
     * @param browserName The name of the browser to check
     */
    private static void checkSpecificBrowserLaunch(String browserName) {
        // Determine the browser type to use
        WebDriverManager.BrowserType browserType;
        try {
            switch (browserName.toLowerCase()) {
                case "firefox":
                    browserType = WebDriverManager.BrowserType.FIREFOX;
                    break;
                case "edge":
                    browserType = WebDriverManager.BrowserType.EDGE;
                    break;
                case "safari":
                    browserType = WebDriverManager.BrowserType.SAFARI;
                    break;
                case "chrome":
                default:
                    browserType = WebDriverManager.BrowserType.CHROME;
            }
            
            System.out.println("Testing " + browserType + " browser launch...");
            
            // Try to launch the browser
            WebDriver driver = null;
            try {
                driver = WebDriverManager.createNewDriver(browserType);
                System.out.println(browserType + " Launch: PASS");
                
                try {
                    driver.get("about:blank");
                    System.out.println("Page Load: PASS");
                } catch (Exception e) {
                    System.out.println("Page Load: FAIL (" + e.getMessage() + ")");
                }
            } catch (Exception e) {
                System.out.println(browserType + " Launch: FAIL (" + e.getMessage() + ")");
            } finally {
                if (driver != null) {
                    try {
                        driver.quit();
                        System.out.println("Browser Quit: PASS");
                    } catch (Exception e) {
                        System.out.println("Browser Quit: FAIL (" + e.getMessage() + ")");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking browser launch: " + e.getMessage());
        }
    }
      /**
     * Try to find browser installation path
     * @param browserType The browser type to find (chrome, firefox, edge, safari)
     * @return The path to the browser executable if found, null otherwise
     */
    private static String getBrowserPath(String browserType) {
        String os = System.getProperty("os.name").toLowerCase();
        
        String[] possiblePaths;
        
        switch (browserType.toLowerCase()) {
            case "chrome":
                if (os.contains("win")) {
                    possiblePaths = new String[] {
                        "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                        "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
                    };
                } else if (os.contains("mac")) {
                    possiblePaths = new String[] {
                        "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
                    };
                } else {
                    possiblePaths = new String[] {
                        "/usr/bin/google-chrome",
                        "/usr/bin/chrome",
                        "/usr/bin/chromium",
                        "/usr/bin/chromium-browser"
                    };
                }
                break;
                
            case "firefox":
                if (os.contains("win")) {
                    possiblePaths = new String[] {
                        "C:\\Program Files\\Mozilla Firefox\\firefox.exe",
                        "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"
                    };
                } else if (os.contains("mac")) {
                    possiblePaths = new String[] {
                        "/Applications/Firefox.app/Contents/MacOS/firefox"
                    };
                } else {
                    possiblePaths = new String[] {
                        "/usr/bin/firefox",
                        "/usr/lib/firefox/firefox"
                    };
                }
                break;
                
            case "edge":
                if (os.contains("win")) {
                    possiblePaths = new String[] {
                        "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe",
                        "C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe"
                    };
                } else if (os.contains("mac")) {
                    possiblePaths = new String[] {
                        "/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge"
                    };
                } else {
                    possiblePaths = new String[] {
                        "/usr/bin/microsoft-edge",
                        "/usr/bin/msedge"
                    };
                }
                break;
                
            case "safari":
                if (os.contains("mac")) {
                    possiblePaths = new String[] {
                        "/Applications/Safari.app/Contents/MacOS/Safari"
                    };
                } else {
                    // Safari is only available on macOS
                    return null;
                }
                break;
                
            default:
                return null;
        }
        
        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                return path;
            }
        }
        
        return null;
    }
}
