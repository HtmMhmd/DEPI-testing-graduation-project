# Swag Labs Automation Testing Project

## Overview
This project contains automated tests for the Swag Labs e-commerce website (https://www.saucedemo.com) using Selenium WebDriver, Java, and JUnit 5. The test suite provides complete coverage of the user journey from login to purchase completion.

## Key Features
- **Page Object Model** for maintainable test code
- **Parallel test execution** for faster test runs
- **Automatic screenshot capture** for failed tests
- **CI/CD integration** via GitHub Actions
- **Support for all user types** (standard, locked_out, problem, performance_glitch, error, visual)

## Getting Started
1. Ensure you have Java 11+ and Maven installed
2. Run the tests using the batch file: `run_tests.bat`
3. Or run with Maven: `mvn clean test`
4. For parallel execution, use: `run_parallel_tests.bat`

## Project Structure
- `src/main/java/com/swaglabs/pages/` - Page objects
- `src/main/java/com/swaglabs/utils/` - Utilities
- `src/test/java/com/swaglabs/tests/` - Test classes
- `docs/` - Comprehensive documentation
- `.github/workflows/` - CI/CD pipeline configuration

## Test Categories
1. Login Tests
2. Product Tests
3. Cart Tests
4. Checkout Tests
5. Navigation Tests
6. User Type Tests

## Documentation
For detailed documentation, please refer to the [docs folder](./docs/README.md).

## Authors
- Omar Shahin
- Hatem Mohamed
