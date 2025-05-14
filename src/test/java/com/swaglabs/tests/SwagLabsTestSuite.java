package com.swaglabs.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test Suite for all Swag Labs automated tests
 * Authors: Omar Shahin, Hatem Mohamed
 */
@Suite
@SelectClasses({
    LoginTests.class,
    ProductTests.class,
    CartTests.class,
    CheckoutTests.class,
    NavigationTests.class
})
public class SwagLabsTestSuite {
    // This class serves as a container for the test suite
}