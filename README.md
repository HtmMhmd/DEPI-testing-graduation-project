# Swag Labs Automation Testing Project

## Overview
This project contains automated tests for the Swag Labs e-commerce website (https://www.saucedemo.com) using Selenium WebDriver, Java, and JUnit 5. The test suite provides complete coverage of the user journey from login to purchase completion.

## Key Features
- **Page Object Model** for maintainable test code
- **Parallel test execution** for faster test runs
- **Automatic screenshot capture** for failed tests
- **CI/CD integration** via GitHub Actions
- **Support for all user types** (standard, locked_out, problem, performance_glitch, error, visual)
- **Headless mode support** for CI/CD environments
- **Environment diagnostics** for easy troubleshooting
- **Browser compatibility** with improved error handling

## Getting Started
1. Ensure you have Java 11+ and Maven installed
2. Run diagnostics to check your environment: `run_diagnostics.bat`
3. Run the tests using the batch file: `run_tests.bat`
4. Or run with Maven: `mvn clean test`
5. For headless execution: `run_tests.bat --headless`
6. For specific test classes: `run_tests.bat --test SimpleTest`
7. For cross-browser testing: `run_browser_tests.bat chrome firefox edge`

## Running Tests
### Using Batch Files

#### Run All Tests
```
run_tests.bat
```

#### Run Tests with Options
```
run_tests.bat --headless
run_tests.bat --test SimpleTest
run_tests.bat --headless --test LoginTests
```

#### Run Environment Diagnostics
```
run_diagnostics.bat
```

### Using PowerShell
```
./Run-Diagnostics.ps1
```

### Using Maven Directly
```
mvn clean test -Dheadless=true
mvn clean test -Dtest=SimpleTest
mvn exec:java -Dexec.mainClass="com.swaglabs.utils.TestEnvironmentDiagnostics"
```

### Cross-Browser Testing

Run the cross-browser testing utility:

#### Using Batch File
```
run_browser_tests.bat chrome firefox edge
run_browser_tests.bat --headless chrome firefox
run_browser_tests.bat --test LoginTests chrome edge
```

#### Using PowerShell
```
.\RunBrowserTests.ps1 -Browsers "chrome,firefox,edge"
.\RunBrowserTests.ps1 -Test "LoginTests" -Browsers "chrome,firefox" -Headless $true
```

#### Using Maven Directly
```
mvn clean test -Dbrowser=chrome -Dtest=AllTests
mvn clean test -Dbrowser=firefox -Dheadless=true
mvn clean test -Dbrowser=edge -Dtest=LoginTests
```

## Project Structure
- `src/main/java/com/swaglabs/pages/` - Page objects
- `src/main/java/com/swaglabs/utils/` - Utilities and WebDriver management
- `src/test/java/com/swaglabs/tests/` - Test classes
- `test-screenshots/` - Screenshots of failed tests
- `docs/` - Comprehensive documentation

## Test Categories
1. Login Tests
2. Product Tests
3. Cart Tests
4. Checkout Tests
5. Navigation Tests
6. User Type Tests

## Troubleshooting
If you encounter issues running the tests:

1. Run the diagnostics tool first: `run_diagnostics.bat`
2. Check if Chrome is installed and up-to-date
3. Verify your internet connection
4. Check if the Swag Labs website is reachable
5. Look for error messages in the console output

## Documentation
For detailed documentation, please refer to the [docs folder](./docs/README.md).

## Authors
- Omar Shahin
- Hatem Mohamed
