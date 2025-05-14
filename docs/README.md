# Swag Labs Automation Testing Project

## Project Overview
This project consists of automated tests for the Swag Labs e-commerce website (https://www.saucedemo.com). The test suite covers the complete user journey from login to checkout, including product browsing, cart management, and responsive design testing.

## Project Structure
The project follows the Page Object Model (POM) design pattern, which separates the test logic from the UI interaction code:

```
src/
├── main/java/com/swaglabs/
│   ├── pages/         # Page objects for each page of the website
│   └── utils/         # Utility classes like WebDriverManager
└── test/java/com/swaglabs/
    └── tests/         # Test classes organized by functionality
```

## Technology Stack
- **Programming Language**: Java
- **Testing Framework**: JUnit 5
- **Automation Tool**: Selenium WebDriver 4.16.1
- **Build Tool**: Maven
- **WebDriver Management**: WebDriverManager 5.5.3
- **Browser**: Chrome

## Test Categories
The test suite is organized into the following categories:

1. **Login Tests**: Authentication functionality
2. **Product Tests**: Product listing, sorting, and details
3. **Cart Tests**: Adding, removing items, and cart persistence
4. **Checkout Tests**: Complete purchase flow
5. **Navigation Tests**: Menu navigation, responsive design, error handling

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

## Additional Resources
- [User Guide](./UserGuide.md) - Instructions for running and maintaining the tests
- [Test Execution Summary](./TestExecutionSummary.md) - Summary of the latest test execution results
- [Project Completion Report](./ProjectCompletionReport.md) - Overview of the project completion status
