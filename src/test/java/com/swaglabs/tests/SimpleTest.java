package com.swaglabs.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Simple test to verify JUnit is working
 */
public class SimpleTest {
    
    @Test
    public void testSimpleAssertion() {
        // Just a simple test to verify JUnit is working
        Assertions.assertTrue(true, "True should be true");
    }
}
