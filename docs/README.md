# Swag Labs Automation Testing Project

## Project Overview
This project consists of automated tests for the Swag Labs e-commerce website (https://www.saucedemo.com). The test suite covers the complete user journey from login to checkout, including product browsing, cart management, and responsive design testing. The framework supports parallel test execution with automatic screenshot capture for failed tests.

## Project Structure
The project follows the Page Object Model (POM) design pattern, which separates the test logic from the UI interaction code:

```
src/
├── main/java/com/swaglabs/
│   ├── pages/                # Page objects for each page of the website
│   │   ├── BasePage.java     # Regular base page
│   │   └── ParallelBasePage.java  # Thread-safe base page
│   └── utils/
│       ├── WebDriverManager.java   # Regular WebDriver manager
│       ├── ThreadLocalWebDriverManager.java  # Thread-safe WebDriver manager
│       └── TestListener.java       # Screenshot capture for failures
├── test/java/com/swaglabs/
│   └── tests/                # Test classes organized by functionality
└── .github/workflows/        # CI/CD pipeline configuration
```

## Technology Stack
- **Programming Language**: Java
- **Testing Framework**: JUnit 5
- **Automation Tool**: Selenium WebDriver 4.16.1
- **Build Tool**: Maven
- **WebDriver Management**: WebDriverManager 5.5.3
- **Browser**: Chrome
- **CI/CD**: GitHub Actions

## Key Features
- **Page Object Model**: Clean separation of test logic and UI interaction
- **Parallel Test Execution**: Run tests concurrently for faster feedback
- **Screenshot Capture**: Automatic capture of screenshots for failed tests
- **Retry Logic**: Automatic retries for flaky tests
- **User Type Testing**: Tests for all user types (standard, locked_out, etc.)
- **CI/CD Integration**: Automated test execution in GitHub Actions pipeline

## Test Categories
The test suite is organized into the following categories:

1. **Login Tests**: Authentication functionality
2. **Product Tests**: Product listing, sorting, and details
3. **Cart Tests**: Adding, removing items, and cart persistence
4. **Checkout Tests**: Complete purchase flow
5. **Navigation Tests**: Menu navigation, responsive design, error handling
6. **User Type Tests**: Testing behavior of different user types (standard, locked out, problem, etc.)

## Running the Tests
To run the tests, you need Java and Maven installed. You can use the provided batch file:

```
run_tests.bat
```

Or run directly with Maven:

```
mvn clean test
```

For more detailed instructions, please refer to the [User Guide](./UserGuide.md).

## Test Reports
Test reports can be found in the `target/surefire-reports` directory after running the tests. A summary of the latest test execution can be found in the [Test Execution Summary](./TestExecutionSummary.md).

## Project Status
The project is complete with all tests passing. For details about the project completion, refer to the [Project Completion Report](./ProjectCompletionReport.md).

## Authors
- Omar Shahin
- Hatem Mohamed

## Detailed Documentation
Detailed documentation for each test category can be found in the following files:

- [Login Tests Documentation](./LoginTests.md)
- [Product Tests Documentation](./ProductTests.md)
- [Cart Tests Documentation](./CartTests.md)
- [Checkout Tests Documentation](./CheckoutTests.md)
- [Navigation Tests Documentation](./NavigationTests.md)
- [User Type Tests Documentation](./UserTypeTests.md)

## Additional Resources
- [User Guide](./UserGuide.md) - Instructions for running and maintaining the tests
- [Test Execution Summary](./TestExecutionSummary.md) - Summary of the latest test execution results
- [Project Completion Report](./ProjectCompletionReport.md) - Overview of the project completion status
