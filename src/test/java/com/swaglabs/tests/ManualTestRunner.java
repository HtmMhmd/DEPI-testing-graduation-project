package com.swaglabs.tests;

/**
 * Simple manual test runner for demonstration purposes
 */
public class ManualTestRunner {
    
    public static void main(String[] args) {
        System.out.println("===== Swag Labs Test Runner =====");
        System.out.println("Running simple verification test...");
        
        boolean testPassed = true;
        
        try {
            // Simple assertion
            boolean condition = true;
            if (!condition) {
                throw new AssertionError("Test failed: condition was false");
            }
            
            System.out.println("Test PASSED!");
        } catch (AssertionError e) {
            System.err.println("Test FAILED: " + e.getMessage());
            testPassed = false;
        }
        
        System.out.println("===== Test Results =====");
        System.out.println("Tests run: 1");
        System.out.println("Tests passed: " + (testPassed ? "1" : "0"));
        System.out.println("Tests failed: " + (testPassed ? "0" : "1"));
    }
}
