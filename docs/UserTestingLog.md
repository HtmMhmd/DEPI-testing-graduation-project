# User Type Testing Logs

## Overview
This document explains how to use the comprehensive user type testing and logging functionality built into the Swag Labs test automation framework. The framework provides detailed logging for each user type (standard_user, locked_out_user, problem_user, etc.) to help identify and troubleshoot issues specific to certain user types.

## Available User Types

The framework tests the following user types:

1. **Standard User** (`standard_user`): Regular user with full functionality
2. **Locked Out User** (`locked_out_user`): User that cannot log in
3. **Problem User** (`problem_user`): User with UI/UX issues
4. **Performance Glitch User** (`performance_glitch_user`): User experiencing slow performance
5. **Error User** (`error_user`): User encountering errors with cart functionality
6. **Visual User** (`visual_user`): User seeing visual glitches

## Running User Type Tests

### Using the Batch File

To run tests for specific user types with detailed logging:

```
run_user_tests.bat [user-type]
```

Options for `[user-type]`:
- `all`: Run tests for all user types (default)
- `standard`: Run only the standard user test
- `locked`: Run only the locked-out user test
- `problem`: Run only the problem user test
- `glitch`: Run only the performance glitch user test
- `error`: Run only the error user test
- `visual`: Run only the visual user test
- `log`: Run comprehensive logging tests for all user types

### Using Maven Directly

Run tests for all user types:
```
mvn clean test -Dtest=UserTypeTests
```

Run tests for a specific user type:
```
mvn clean test -Dtest=UserTypeTests#testStandardUser
mvn clean test -Dtest=UserTypeTests#testLockedOutUser
mvn clean test -Dtest=UserTypeTests#testProblemUser
mvn clean test -Dtest=UserTypeTests#testPerformanceGlitchUser
mvn clean test -Dtest=UserTypeTests#testErrorUser
mvn clean test -Dtest=UserTypeTests#testVisualUser
```

Run comprehensive logging tests:
```
mvn clean test -Dtest=UserTypeLogTests
```

## Log File Structure

After running the tests, log files will be available in the `logs` directory:

- `user-type-tests.log`: Log entries from all user type tests
- `standard-user.log`: Log entries specific to standard_user
- `locked-out-user.log`: Log entries specific to locked_out_user
- `problem-user.log`: Log entries specific to problem_user
- `performance-glitch-user.log`: Log entries specific to performance_glitch_user
- `error-user.log`: Log entries specific to error_user
- `visual-user.log`: Log entries specific to visual_user
- `test-summary.md`: Markdown summary of all test executions

## Test Summary Report

After test execution, a summary report is generated in `logs/test-summary.md`. This report provides an overview of all test executions, including:

- Test name
- Result (PASS/FAIL)
- Execution time
- Additional notes or error messages

Example summary report:

```markdown
# Swag Labs User Type Test Summary

Generated: 2025-05-15 14:30:45

## standard_user

| Test Name | Result | Execution Time | Notes |
|-----------|--------|----------------|-------|
| Standard User Login Flow | ✅ PASS | 2025-05-15 14:25:12 | All steps completed successfully |

## locked_out_user

| Test Name | Result | Execution Time | Notes |
|-----------|--------|----------------|-------|
| Locked Out User Login Test | ✅ PASS | 2025-05-15 14:26:03 | All steps completed successfully |

## problem_user

| Test Name | Result | Execution Time | Notes |
|-----------|--------|----------------|-------|
| Problem User Login & UI Issues Test | ✅ PASS | 2025-05-15 14:27:22 | All steps completed successfully |
```

## Log Entry Format

Each log entry includes:

- Timestamp
- Thread ID
- Log level (INFO, WARN, ERROR)
- Logger name (for identifying the user type)
- Message

Example log entries:

```
2025-05-15 14:25:10.123 [main] INFO com.swaglabs.user.STANDARD_USER - START TEST: Standard User Login Flow for standard_user
2025-05-15 14:25:10.456 [main] INFO com.swaglabs.user.STANDARD_USER - STEP: Logging in as standard user
2025-05-15 14:25:11.789 [main] INFO com.swaglabs.user.STANDARD_USER - STEP: Verifying redirect to products page
2025-05-15 14:25:12.012 [main] INFO com.swaglabs.user.STANDARD_USER - On products page: true
```

## Using Logs for Analysis

The logs can be used for:

1. **Debugging issues specific to user types**: By examining the logs for a specific user type, you can identify where issues occur.

2. **Performance analysis**: The logs for the performance_glitch_user include timing information to help diagnose slow operations.

3. **Comparing behavior across user types**: By comparing the logs from different user types, you can identify differences in behavior.

4. **Tracking test execution**: The logs provide a detailed record of test execution, making it easier to understand what happened during a test run.

## Implementing Custom Logging

To add custom logging to your own tests:

```java
import com.swaglabs.enums.UserType;
import com.swaglabs.utils.TestLogger;

// Log the start of the test
TestLogger.logTestStart(UserType.STANDARD_USER, "My Custom Test");

// Log a test step
TestLogger.logTestStep(UserType.STANDARD_USER, "Performing action X");

// Log information
TestLogger.logInfo(UserType.STANDARD_USER, "Value of variable: " + someValue);

// Log a warning
TestLogger.logWarning(UserType.STANDARD_USER, "Potential issue detected");

// Log an error
try {
    // Some operation
} catch (Exception e) {
    TestLogger.logError(UserType.STANDARD_USER, "Operation failed", e);
}

// Log the end of the test
TestLogger.logTestEnd(UserType.STANDARD_USER, "My Custom Test", true, "Test completed successfully");
```

## Conclusion

The user type logging feature provides detailed insights into how different user types interact with the Swag Labs application. By analyzing these logs, you can better understand and diagnose user-specific issues, ensuring that your application works correctly for all user types.
