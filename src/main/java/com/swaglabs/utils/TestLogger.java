package com.swaglabs.utils;

import com.swaglabs.enums.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for test logging
 */
public class TestLogger {
    private static final Logger MAIN_LOGGER = LogManager.getLogger(TestLogger.class);
    private static final String LOG_DIR = "logs";
    private static final Map<UserType, Logger> userLoggers = new HashMap<>();
    private static final Map<UserType, Map<String, TestResult>> testResults = new HashMap<>();

    static {
        // Create the logs directory if it doesn't exist
        try {
            Path logPath = Paths.get(LOG_DIR);
            if (!Files.exists(logPath)) {
                Files.createDirectories(logPath);
            }
        } catch (IOException e) {
            MAIN_LOGGER.error("Failed to create logs directory: {}", e.getMessage());
        }
        
        // Initialize user loggers
        for (UserType userType : UserType.values()) {
            userLoggers.put(userType, 
                LogManager.getLogger("com.swaglabs.user." + userType.name()));
            testResults.put(userType, new HashMap<>());
        }
    }
    
    /**
     * Get the logger for a specific user type
     * @param userType The user type
     * @return The logger for the user type
     */
    public static Logger getUserLogger(UserType userType) {
        return userLoggers.get(userType);
    }
    
    /**
     * Log the start of a test case for a specific user type
     * @param userType The user type
     * @param testName The name of the test case
     */
    public static void logTestStart(UserType userType, String testName) {
        Logger logger = getUserLogger(userType);
        logger.info("======================================================");
        logger.info("START TEST: {} for {}", testName, userType.getUsername());
        logger.info("======================================================");
    }
    
    /**
     * Log the end of a test case for a specific user type
     * @param userType The user type
     * @param testName The name of the test case
     * @param result The test result (PASS/FAIL)
     * @param message Optional result message
     */
    public static void logTestEnd(UserType userType, String testName, boolean result, String message) {
        Logger logger = getUserLogger(userType);
        String resultStr = result ? "PASS" : "FAIL";
        
        logger.info("======================================================");
        logger.info("END TEST: {} for {} - {}", testName, userType.getUsername(), resultStr);
        if (message != null && !message.isEmpty()) {
            logger.info("Message: {}", message);
        }
        logger.info("======================================================");
        
        // Store the result for the summary
        testResults.get(userType).put(testName, new TestResult(resultStr, message, LocalDateTime.now()));
    }
    
    /**
     * Log a test step for a specific user type
     * @param userType The user type
     * @param step The step description
     */
    public static void logTestStep(UserType userType, String step) {
        Logger logger = getUserLogger(userType);
        logger.info("STEP: {}", step);
    }
    
    /**
     * Log detailed test information 
     * @param userType The user type
     * @param message The log message
     */
    public static void logInfo(UserType userType, String message) {
        Logger logger = getUserLogger(userType);
        logger.info(message);
    }
    
    /**
     * Log a warning for a specific user type
     * @param userType The user type
     * @param message The warning message
     */
    public static void logWarning(UserType userType, String message) {
        Logger logger = getUserLogger(userType);
        logger.warn("WARNING: {}", message);
    }
    
    /**
     * Log an error for a specific user type
     * @param userType The user type
     * @param message The error message
     * @param e The exception
     */
    public static void logError(UserType userType, String message, Throwable e) {
        Logger logger = getUserLogger(userType);
        logger.error("ERROR: {}", message, e);
    }
    
    /**
     * Generate and save a summary of all test results
     */
    public static void generateTestSummary() {
        StringBuilder summary = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        summary.append("# Swag Labs User Type Test Summary\n\n");
        summary.append("Generated: ").append(LocalDateTime.now().format(formatter)).append("\n\n");
        
        for (UserType userType : UserType.values()) {
            summary.append("## ").append(userType.getUsername()).append("\n\n");
            
            Map<String, TestResult> results = testResults.get(userType);
            if (results.isEmpty()) {
                summary.append("No tests executed for this user type.\n\n");
                continue;
            }
            
            summary.append("| Test Name | Result | Execution Time | Notes |\n");
            summary.append("|-----------|--------|----------------|-------|\n");
            
            for (Map.Entry<String, TestResult> entry : results.entrySet()) {
                TestResult result = entry.getValue();
                summary.append("| ").append(entry.getKey()).append(" | ");
                
                if ("PASS".equals(result.getResult())) {
                    summary.append("✅ PASS");
                } else {
                    summary.append("❌ FAIL");
                }
                
                summary.append(" | ").append(result.getTime().format(formatter)).append(" | ");
                
                if (result.getMessage() != null && !result.getMessage().isEmpty()) {
                    summary.append(result.getMessage());
                }
                
                summary.append(" |\n");
            }
            
            summary.append("\n");
        }
        
        // Save summary to file
        try {
            Path summaryPath = Paths.get(LOG_DIR, "test-summary.md");
            Files.write(summaryPath, summary.toString().getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            MAIN_LOGGER.info("Test summary generated: {}", summaryPath.toAbsolutePath());
        } catch (IOException e) {
            MAIN_LOGGER.error("Failed to generate test summary: {}", e.getMessage());
        }
    }
    
    /**
     * Inner class to store test results
     */
    private static class TestResult {
        private final String result;
        private final String message;
        private final LocalDateTime time;
        
        public TestResult(String result, String message, LocalDateTime time) {
            this.result = result;
            this.message = message;
            this.time = time;
        }
        
        public String getResult() {
            return result;
        }
        
        public String getMessage() {
            return message;
        }
        
        public LocalDateTime getTime() {
            return time;
        }
    }
}
