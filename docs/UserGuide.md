# Swag Labs Test Automation User Guide

## Overview
This user guide provides instructions on how to run the automated tests for the Swag Labs e-commerce website and interpret the results. The test suite covers the complete user journey from login to checkout.

## Prerequisites
Before running the tests, ensure you have the following installed:

1. **Java Development Kit (JDK)** - version 11 or higher
   - Verify installation: `java -version`

2. **Maven** - version 3.6 or higher
   - Verify installation: `mvn -version`

3. **Chrome Browser** - latest stable version
   - The tests use Chrome by default

## Running the Tests

### Method 1: Using the Batch File (Windows)
For Windows users, a batch file is provided for easy execution:

1. Navigate to the project root directory
2. Double-click the `run_tests.bat` file
   - This will open a command prompt window and execute the tests
   - The window will remain open after test execution for you to view the results

### Method 2: Using Maven Command
For more control over the test execution, you can use Maven commands:

1. Open a terminal/command prompt
2. Navigate to the project root directory
3. Run all tests:
   ```
   mvn clean test
   ```
4. Run a specific test class:
   ```
   mvn clean test -Dtest=LoginTests
   ```
5. Run a specific test method:
   ```
   mvn clean test -Dtest=LoginTests#testValidLogin
   ```

### Method 3: Using an IDE
If you're using an IDE like IntelliJ IDEA or Eclipse:

1. Open the project in your IDE
2. Right-click on the test class or method you want to run
3. Select "Run" or "Debug" option

## Interpreting the Results

### Console Output
After running the tests, you'll see console output showing:
- Test execution progress
- Any errors or failures
- Summary of test results (pass/fail count)

### Test Reports
Detailed test reports can be found in the following locations:

1. **Surefire Reports** - `target/surefire-reports/`
   - Contains TXT and XML files with detailed test results
   - XML files can be used by CI/CD tools like Jenkins

2. **HTML Reports** (if generated) - `target/site/surefire-report.html`
   - To generate HTML reports:
     ```
     mvn surefire-report:report
     ```

## Understanding Test Categories

The test suite is organized into the following categories:

1. **Login Tests (`LoginTests.java`)**
   - Tests user authentication functionality
   - Validates error messages for invalid credentials

2. **Product Tests (`ProductTests.java`)**
   - Tests product listing and detail pages
   - Validates sorting functionality

3. **Cart Tests (`CartTests.java`)**
   - Tests adding and removing products from the cart
   - Validates cart persistence during navigation

4. **Checkout Tests (`CheckoutTests.java`)**
   - Tests the complete checkout flow
   - Validates form validation and order completion

5. **Navigation Tests (`NavigationTests.java`)**
   - Tests menu navigation and browser controls
   - Validates responsive design elements

## Troubleshooting

### Common Issues and Solutions

1. **Tests fail with "Driver not found" error**
   - Ensure Chrome is installed and up-to-date
   - Check that WebDriverManager can download the correct driver

2. **Tests fail with "Element not found" error**
   - This may occur if the website structure has changed
   - Check the locators in the Page Object classes

3. **Tests run slowly**
   - Consider running specific test categories instead of the entire suite
   - Ensure your system meets the minimum requirements

4. **Maven cannot download dependencies**
   - Check your internet connection
   - Verify that your Maven settings.xml file is properly configured

## Getting Help
For additional help or to report issues:

1. Check the comprehensive documentation in the `docs` folder
2. Contact the project maintainers:
   - Omar Shahin
   - Hatem Mohamed
