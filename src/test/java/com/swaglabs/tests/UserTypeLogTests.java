package com.swaglabs.tests;

import com.swaglabs.enums.UserType;
import com.swaglabs.utils.TestLogger;
import org.junit.jupiter.api.*;

/**
 * Test class to run comprehensive tests for all user types with detailed logging
 */
public class UserTypeLogTests {
    
    private UserTypeTests userTypeTests;
    
    @BeforeEach
    public void setup() {
        userTypeTests = new UserTypeTests();
    }
    
    @AfterAll
    public static void generateReport() {
        TestLogger.generateTestSummary();
    }
    
    /**
     * Run all tests for standard user
     */
    @Test
    @DisplayName("Run All Tests for Standard User")
    public void testStandardUserComprehensive() {
        userTypeTests.setup();
        try {
            userTypeTests.runAllTestsForUserType(UserType.STANDARD_USER);
        } finally {
            userTypeTests.tearDown();
        }
    }
    
    /**
     * Run all tests for locked out user
     */
    @Test
    @DisplayName("Run All Tests for Locked Out User")
    public void testLockedOutUserComprehensive() {
        userTypeTests.setup();
        try {
            userTypeTests.runAllTestsForUserType(UserType.LOCKED_OUT_USER);
        } finally {
            userTypeTests.tearDown();
        }
    }
    
    /**
     * Run all tests for problem user
     */
    @Test
    @DisplayName("Run All Tests for Problem User")
    public void testProblemUserComprehensive() {
        userTypeTests.setup();
        try {
            userTypeTests.runAllTestsForUserType(UserType.PROBLEM_USER);
        } finally {
            userTypeTests.tearDown();
        }
    }
    
    /**
     * Run all tests for performance glitch user
     */
    @Test
    @DisplayName("Run All Tests for Performance Glitch User")
    public void testPerformanceGlitchUserComprehensive() {
        userTypeTests.setup();
        try {
            userTypeTests.runAllTestsForUserType(UserType.PERFORMANCE_GLITCH_USER);
        } finally {
            userTypeTests.tearDown();
        }
    }
    
    /**
     * Run all tests for error user
     */
    @Test
    @DisplayName("Run All Tests for Error User")
    public void testErrorUserComprehensive() {
        userTypeTests.setup();
        try {
            userTypeTests.runAllTestsForUserType(UserType.ERROR_USER);
        } finally {
            userTypeTests.tearDown();
        }
    }
    
    /**
     * Run all tests for visual user
     */
    @Test
    @DisplayName("Run All Tests for Visual User")
    public void testVisualUserComprehensive() {
        userTypeTests.setup();
        try {
            userTypeTests.runAllTestsForUserType(UserType.VISUAL_USER);
        } finally {
            userTypeTests.tearDown();
        }
    }
    
    /**
     * Run a quick test to check all login capabilities
     */
    @Test
    @DisplayName("Run Login Tests for All User Types")
    public void testAllUserTypeLogins() {
        for (UserType userType : UserType.values()) {
            userTypeTests.setup();
            TestLogger.logInfo(userType, "Testing login for: " + userType.getUsername());
            try {
                // We only need the basics per user type for this test
                switch (userType) {
                    case STANDARD_USER:
                        userTypeTests.testStandardUser();
                        break;
                    case LOCKED_OUT_USER:
                        userTypeTests.testLockedOutUser();
                        break;
                    case PROBLEM_USER:
                        userTypeTests.testProblemUser();
                        break;
                    case PERFORMANCE_GLITCH_USER:
                        userTypeTests.testPerformanceGlitchUser();
                        break;
                    case ERROR_USER:
                        userTypeTests.testErrorUser();
                        break;
                    case VISUAL_USER:
                        userTypeTests.testVisualUser();
                        break;
                }
            } catch (Exception e) {
                TestLogger.logError(userType, "Failed to run login test", e);
            } finally {
                userTypeTests.tearDown();
            }
        }
    }
}
