# System Analysis & Design

## Problem Statement & Objectives

### Problem Statement

Testing the Swag Labs e-commerce application presents several challenges that manual testing alone cannot efficiently address:

1. **Multiple User Types**: The application has six different user types (standard_user, locked_out_user, problem_user, performance_glitch_user, error_user, visual_user), each with unique behaviors that must be verified.

2. **Cross-Browser Compatibility**: Ensuring consistent behavior across Chrome, Firefox, Edge, and Safari requires repetitive testing scenarios that are time-consuming when performed manually.

3. **Regression Testing**: Changes to the application require repeated verification of existing functionality, which becomes unsustainable as the application grows.

4. **Consistency and Reliability**: Manual testing is prone to human error and inconsistency, particularly when testing complex workflows like the checkout process.

5. **Test Coverage**: Achieving comprehensive test coverage across all user types, browsers, and functionalities is challenging without automation.

### Project Objectives

1. **Develop a Robust Test Framework**: Create a Selenium WebDriver-based framework using Java and JUnit 5 that provides a foundation for all automated tests.

2. **Implement Page Object Model**: Design a maintainable architecture using the Page Object Model pattern to separate test logic from page implementation details.

3. **Enable Cross-Browser Testing**: Implement capabilities to execute tests across Chrome, Firefox, Edge, and Safari with consistent behavior.

4. **Support Parallel Execution**: Optimize test execution time through parallel test execution with proper thread management.

5. **Ensure User Type Coverage**: Create tests that verify application behavior for all six user types.

6. **Integrate with CI/CD**: Configure the test suite to run within GitHub Actions workflows for continuous testing.

7. **Provide Detailed Reporting**: Implement test reporting mechanisms with screenshots, logs, and failure details.

8. **Develop Diagnostic Tools**: Create tools to verify proper environment setup and diagnose common issues.

## Use Case Diagram & Descriptions

### Use Case Diagram

```
+-----------------------------------+
|       Swag Labs Test System       |
+-----------------------------------+
                   |
       +-----------+------------+
       |                        |
+------v-------+        +-------v------+
| QA Engineer  |        | CI/CD System |
+------+-------+        +-------+------+
       |                        |
       |  +-------------------+ |
       +->| Run All Tests     |<+
       |  +-------------------+
       |
       |  +-------------------+
       +->| Run Specific Tests|
       |  +-------------------+
       |
       |  +-------------------+
       +->| Run Cross-Browser |
       |  +-------------------+
       |
       |  +-------------------+
       +->| Generate Reports  |
       |  +-------------------+
       |
       |  +-------------------+
       +->| Run Diagnostics   |
          +-------------------+
```

### Use Case Descriptions

#### Use Case 1: Run All Tests

**Actor**: QA Engineer or CI/CD System  
**Description**: Execute the complete test suite to verify all functionality  
**Preconditions**: Test environment is properly configured  
**Main Flow**:
1. Actor executes the run_tests.bat script or Maven command
2. System initializes the WebDriver based on configuration
3. System executes all test cases in the appropriate order
4. System captures results and generates reports
**Postconditions**: Test reports are generated with pass/fail status
**Alternative Flows**:
- If environment is not properly configured, diagnostics information is displayed
- If tests fail, screenshots and error logs are captured

#### Use Case 2: Run Specific Tests

**Actor**: QA Engineer  
**Description**: Execute a subset of tests for specific features  
**Preconditions**: Test environment is properly configured  
**Main Flow**:
1. Actor executes command with specific test class or method parameters
2. System initializes the WebDriver based on configuration
3. System executes only the specified tests
4. System captures results and generates reports
**Postconditions**: Test reports are generated for the specified tests
**Alternative Flows**:
- If specified tests don't exist, appropriate error message is displayed

#### Use Case 3: Run Cross-Browser Tests

**Actor**: QA Engineer  
**Description**: Execute tests across multiple browsers  
**Preconditions**: Required browsers are installed on the system  
**Main Flow**:
1. Actor executes run_browser_tests.bat with desired browser parameters
2. System sequentially initializes each browser's WebDriver
3. System executes tests on each browser
4. System captures browser-specific results
**Postconditions**: Browser-specific test reports are generated
**Alternative Flows**:
- If a specified browser is not installed, appropriate error is logged

#### Use Case 4: Generate Reports

**Actor**: QA Engineer or CI/CD System  
**Description**: Generate comprehensive test execution reports  
**Preconditions**: Tests have been executed  
**Main Flow**:
1. System compiles test execution results
2. System formats results into HTML reports
3. System includes screenshots for failed tests
4. System calculates test metrics and statistics
**Postconditions**: HTML reports are available for review

#### Use Case 5: Run Diagnostics

**Actor**: QA Engineer  
**Description**: Verify environment setup and diagnose issues  
**Preconditions**: System has access to browser binaries  
**Main Flow**:
1. Actor executes run_diagnostics.bat script
2. System checks Java version and configuration
3. System verifies browser installations
4. System tests network connectivity
5. System validates WebDriver functionality
**Postconditions**: Diagnostic report is displayed with environment status

## Functional & Non-Functional Requirements

### Functional Requirements

1. **Test Execution**:
   - Support for running all tests or specific test cases
   - Command-line and batch file execution options
   - Configuration options for browser selection and headless mode

2. **Browser Automation**:
   - Chrome, Firefox, Edge, and Safari support
   - Headless mode for CI/CD pipeline execution
   - Screenshot capture on test failures

3. **Test Organization**:
   - Logical grouping of tests by functionality
   - Support for test suites and categories
   - Parameterized tests for different user types

4. **Reporting**:
   - HTML test reports with execution details
   - Screenshot capture for failed tests
   - Test execution metrics and statistics

5. **Diagnostics**:
   - Environment verification tools
   - Network connectivity checks
   - Browser installation validation

### Non-Functional Requirements

1. **Performance**:
   - Test suite execution within 15 minutes
   - Efficient resource utilization during parallel execution
   - Minimal setup time per test

2. **Reliability**:
   - Consistent test results across multiple runs
   - Proper handling of dynamic elements and timing issues
   - Graceful handling of network fluctuations

3. **Usability**:
   - Clear documentation and comments
   - Intuitive command structure
   - Actionable error messages and logs

4. **Maintainability**:
   - Modular code structure using Page Object Model
   - Consistent coding standards
   - Separation of concerns between test logic and page implementation

5. **Scalability**:
   - Support for adding new tests and test types
   - Extendable for additional browsers
   - Configurable parallel execution capacity

## Software Architecture

### Architecture Overview

The Swag Labs Test Framework follows a layered architecture with the following components:

1. **Test Layer**: JUnit 5 test classes defining test cases and scenarios
2. **Page Object Layer**: Page classes representing application screens and their interactions
3. **Utility Layer**: Support classes for WebDriver management, test listeners, and helpers
4. **Configuration Layer**: Properties and settings for test execution
5. **Reporting Layer**: Test result capture and report generation

### Architecture Diagram

```
+---------------------------------------------------------------+
|                        Test Layer                             |
| +---------------------+ +---------------+ +------------------+ |
| | UserTypeTests       | | LoginTests    | | CheckoutTests    | |
| +---------------------+ +---------------+ +------------------+ |
+---------------------------------------------------------------+
                           |
                           | uses
                           v
+---------------------------------------------------------------+
|                     Page Object Layer                         |
| +---------------+ +---------------+ +----------------------+   |
| | LoginPage     | | ProductsPage  | | CheckoutPage         |   |
| +---------------+ +---------------+ +----------------------+   |
| +---------------+ +---------------+ +----------------------+   |
| | CartPage      | | ProductDetails| | BasePage             |   |
| +---------------+ +---------------+ +----------------------+   |
+---------------------------------------------------------------+
                           |
                           | uses
                           v
+---------------------------------------------------------------+
|                      Utility Layer                            |
| +------------------+ +----------------+ +-------------------+  |
| | WebDriverManager | | TestListener   | | TestEnvironment   |  |
| +------------------+ +----------------+ | Diagnostics       |  |
|                      +----------------+ +-------------------+  |
|                      | ThreadLocalWeb |                       |
|                      | DriverManager  |                       |
|                      +----------------+                       |
+---------------------------------------------------------------+
                           |
                           | uses
                           v
+---------------------------------------------------------------+
|                 Configuration & Reporting                     |
| +------------------+ +----------------+ +-------------------+  |
| | Properties       | | Test Reports   | | Screenshots       |  |
| +------------------+ +----------------+ +-------------------+  |
+---------------------------------------------------------------+
```

### Architecture Style: Page Object Model (POM) with Test Utilities

The framework follows the Page Object Model architecture pattern, which:
- Separates page-specific elements and actions into dedicated classes
- Abstracts UI interactions from test logic
- Provides reusable components for different test scenarios
- Improves maintainability by centralizing page-specific changes

## Class Diagram

```
+---------------------+      +--------------------+
| BasePage            |<-----|   LoginPage        |
+---------------------+      +--------------------+
| - driver: WebDriver |      | - usernameField    |
| - wait: WebDriverWait|     | - passwordField    |
+---------------------+      | - loginButton      |
| + click()           |      +--------------------+
| + type()            |      | + loginAs()        |
| + isElementPresent()|      | + getErrorMessage()|
| + waitForElement()  |      +--------------------+
+---------------------+             ^
         ^                          |
         |                          |
+---------------------+      +--------------------+
| ProductsPage        |      | CheckoutPage       |
+---------------------+      +--------------------+
| - productItems      |      | - firstNameField   |
| - sortDropdown      |      | - lastNameField    |
| - cartLink          |      | - zipCodeField     |
+---------------------+      +--------------------+
| + addToCart()       |      | + enterFirstName() |
| + sortProducts()    |      | + enterLastName()  |
| + goToCart()        |      | + enterZipCode()   |
+---------------------+      | + clickContinue()  |
                            +--------------------+

+------------------------+     +---------------------+
| WebDriverManager       |     | TestListener        |
+------------------------+     +---------------------+
| - driver: ThreadLocal<>|     | + testFailed()      |
| - HEADLESS_MODE: bool  |     | + captureScreenshot()|
| - CI_MODE: bool        |     +---------------------+
+------------------------+
| + setupDriver()        |     +---------------------+
| + getDriver()          |     | TestEnvironment     |
| + quitDriver()         |     | Diagnostics         |
| + createNewDriver()    |     +---------------------+
| + isReachable()        |     | + checkJavaVersion()|
+------------------------+     | + checkBrowsers()   |
                               | + checkConnectivity()|
+----------------------+       +---------------------+
| BrowserType          |
+----------------------+
| CHROME               |
| FIREFOX              |
| EDGE                 |
| SAFARI               |
+----------------------+

+--------------------+
| UserType           |
+--------------------+
| STANDARD_USER      |
| LOCKED_OUT_USER    |
| PROBLEM_USER       |
| PERFORMANCE_GLITCH_|
| ERROR_USER         |
| VISUAL_USER        |
+--------------------+
| + getUsername()    |
| + getPassword()    |
+--------------------+
```

## Component Diagram

```
                      +------------------------+
                      |    Test Execution      |
                      |      Components        |
                      +------------------------+
                           /           \
                          /             \
          +---------------+           +-----------------+
          | JUnit 5 Tests |           | Maven/Batch     |
          +---------------+           | Execution       |
                |                     +-----------------+
                |                            |
                v                            v
       +------------------+         +------------------+
       |  Page Objects    |<------->|  Test Utilities  |
       +------------------+         +------------------+
                                           |
                                           v
                                 +--------------------+
                                 | WebDriverManager   |
                                 +--------------------+
                                      /        \
                                     /          \
                      +------------+             +-------------+
                      | Browser    |             | Network &   |
                      | Components |             | Diagnostics |
                      +------------+             +-------------+
```

## Data Flow Diagram

### Context Level DFD

```
                 +----------------+
    Config       |                |        Test Results
   ------------>+|     Test       |+--------------->
                 | Automation     |
   Browser Type  |    System      | Screenshots
   ------------>+|                |+--------------->
                 +----------------+
                        |
                        v
                 +----------------+
                 |   Swag Labs    |
                 |  Application   |
                 |  Under Test    |
                 +----------------+
```

### Level 1 DFD

```
   Test Commands    +-------------+
  --------------->  | Test Runner |  ----------+
                   +-------------+            |
                         |                    |
                         v                    v
  Test Data      +----------------+    +-------------+
 ------------->  | Test Execution |    | Reporting & |
                 | & Validation   |    | Diagnostics |
                 +----------------+    +-------------+
                         |                    ^
                         v                    |
                 +----------------+           |
                 | WebDriver      |           |
                 | Management     | ----------+
                 +----------------+      Test Results
                         |
                         v
                 +----------------+
                 |  Browser       |
                 | Interaction    |
                 +----------------+
                         |
                         v
                 +----------------+
                 |   Swag Labs    |
                 |  Application   |
                 +----------------+
```

## Sequence Diagram: Login Test Flow

```
+--------+      +-----------+      +---------------+     +----------------+
| Test   |      | LoginPage |      | WebDriver     |     | Swag Labs App  |
+--------+      +-----------+      +---------------+     +----------------+
    |                |                    |                     |
    | loginAs()      |                    |                     |
    |--------------->|                    |                     |
    |                | type(username)     |                     |
    |                |------------------->|                     |
    |                |                    | sendKeys()          |
    |                |                    |-------------------->|
    |                |                    |                     |
    |                | type(password)     |                     |
    |                |------------------->|                     |
    |                |                    | sendKeys()          |
    |                |                    |-------------------->|
    |                |                    |                     |
    |                | click(loginBtn)    |                     |
    |                |------------------->|                     |
    |                |                    | click()             |
    |                |                    |-------------------->|
    |                |                    |                     |
    |                | wait(productsPage) |                     |
    |                |------------------->|                     |
    |                |                    | wait until loaded   |
    |                |                    |<------------------->|
    |                |                    |                     |
    |<-- ProductsPage|                    |                     |
    |                |                    |                     |
```

## Activity Diagram: Complete Checkout Process

```
     +----------------+
     | Start Test     |
     +----------------+
             |
             v
     +----------------+
     | Login as User  |
     +----------------+
             |
             v
     +----------------+
     | Add Product    |
     | to Cart        |
     +----------------+
             |
             v
     +----------------+
     | Go to Cart     |
     +----------------+
             |
             v
     +----------------+
     | Click Checkout |
     +----------------+
             |
             v
     +----------------+
     | Enter Customer |
     | Information    |
     +----------------+
             |
             v
     +----------------+
     | Click Continue |
     +----------------+
             |
             v
     +----------------+
     | Verify Checkout|
     | Overview Page  |
     +----------------+
             |
             v
     +----------------+
     | Click Finish   |
     +----------------+
             |
             v
     +----------------+
     | Verify Order   |
     | Complete       |
     +----------------+
             |
             v
     +----------------+
     | End Test       |
     +----------------+
```

## State Diagram: WebDriver Lifecycle

```
     +----------------+
     | Not Initialized|
     +----------------+
             |
             | setupDriver()
             v
     +----------------+
     | Driver Ready   |
     +----------------+
             |
             | navigateToBaseUrl()
             v
     +----------------+
     | On Login Page  |<-----------+
     +----------------+            |
             |                     |
             | login()             |
             v                     |
     +----------------+            |
     | On Products    |            | logout()
     | Page           |------------+
     +----------------+
             |
             | quitDriver()
             v
     +----------------+
     | Driver Closed  |
     +----------------+
```

## Deployment Diagram

```
    +---------------------------------+
    |      Developer Workstation      |
    |-------------------------------- |
    |                                 |
    |  +----------+   +-----------+   |
    |  | Java JDK |   | Maven     |   |
    |  +----------+   +-----------+   |
    |                                 |
    |  +----------+   +-----------+   |
    |  | Browser  |   | WebDriver |   |
    |  | Binaries |   | Executables|  |
    |  +----------+   +-----------+   |
    |                                 |
    |  +----------+                   |
    |  | Test     |                   |
    |  | Framework|                   |
    |  +----------+                   |
    +---------------------------------+
                |
                | Git Push
                v
    +---------------------------------+
    |         GitHub Actions          |
    |-------------------------------- |
    |                                 |
    |  +----------+   +-----------+   |
    |  | Runner   |   | Ubuntu    |   |
    |  | Container|   | Linux     |   |
    |  +----------+   +-----------+   |
    |                                 |
    |  +----------+   +-----------+   |
    |  | Headless |   | Maven     |   |
    |  | Chrome   |   | Build     |   |
    |  +----------+   +-----------+   |
    |                                 |
    |  +----------+                   |
    |  | Test     |                   |
    |  | Execution|                   |
    |  +----------+                   |
    +---------------------------------+
                |
                | Generate
                v
    +---------------------------------+
    |        Test Artifacts           |
    |-------------------------------- |
    |                                 |
    |  +----------+   +-----------+   |
    |  | Test     |   | Screenshots|  |
    |  | Reports  |   | & Logs    |   |
    |  +----------+   +-----------+   |
    |                                 |
    +---------------------------------+
```

## UI/UX Design & Prototyping

As a test automation framework, the UI/UX is primarily focused on the command-line interface and reporting outputs:

### Command Line Interface

The framework offers several batch files for ease of use:

1. **run_tests.bat**: Main entry point for standard test execution
   ```
   Usage: run_tests.bat [options]
     Options:
       --headless    Run in headless mode
       --browser=X   Specify browser (chrome, firefox, edge, safari)
       --test=X      Run specific test class
   ```

2. **run_diagnostics.bat**: Tool for verifying environment setup
   ```
   Usage: run_diagnostics.bat [options]
     Options:
       --verbose     Show detailed diagnostic information
   ```

3. **run_browser_tests.bat**: For cross-browser testing
   ```
   Usage: run_browser_tests.bat [options]
     Options:
       --browsers="chrome,firefox,edge"  Specify browsers to test
       --parallel                        Run browsers in parallel
   ```

### Reports & Output

1. **HTML Test Reports**: Clean, organized reports showing test execution results
   - Test case summary with pass/fail status
   - Execution time and performance metrics
   - Failure details with screenshots
   - Filter and search capabilities

2. **Log Output**: Structured logs with:
   - Timestamp for each action
   - Clear indication of test steps
   - Error messages and stack traces when failures occur
   - Browser and environment information

3. **Screenshots**: Automatically captured on test failures
   - Named with test case and timestamp
   - Organized in a dedicated screenshot directory
   - Referenced in HTML reports for easy access

## Technology Stack

### Backend
- **Java 11**: Programming language for test implementation
- **Maven**: Build and dependency management
- **JUnit 5**: Test framework for test organization and execution
- **Selenium WebDriver 4.x**: Browser automation library
- **WebDriverManager**: Automatic driver management utility

### Frontend (Test Application)
- **Swag Labs**: Web application under test
- **Browsers**: Chrome, Firefox, Edge, Safari

### Testing & QA
- **Maven Surefire**: Test execution plugin
- **Maven Surefire Report**: Test reporting plugin
- **JUnit 5 Params**: Parameterized testing support
- **Screenshot Capture**: Custom implementation for failure documentation

### Development Tools
- **Git**: Version control
- **GitHub**: Repository hosting
- **GitHub Actions**: CI/CD integration
- **VS Code/IntelliJ**: IDE for test development

### Deployment & Environment
- **Batch Scripts**: Simplified test execution
- **PowerShell Scripts**: Enhanced automation capabilities
- **Windows/Linux/MacOS**: Cross-platform support

## Additional Deliverables

### API Documentation

While this framework focuses on UI testing, key utilities and framework components are documented:

1. **WebDriverManager**:
   - `setupDriver()`: Initializes WebDriver based on configuration
   - `getDriver()`: Returns the current WebDriver instance
   - `quitDriver()`: Properly closes and cleans up WebDriver resources
   - `createNewDriver(BrowserType)`: Creates a new driver for specified browser
   - `isSwagLabsReachable()`: Checks connectivity to the application

2. **TestListener**:
   - `testFailed(ExtensionContext)`: Handles test failure actions like screenshot capture
   - `captureScreenshot(String)`: Captures and saves a screenshot with the given name

3. **Page Objects**:
   - `BasePage`: Common methods for all pages
   - Specific page classes with fluent API for test interactions

### Testing & Validation

1. **Unit Tests**:
   - Framework utility tests
   - Helper method validations

2. **Integration Tests**:
   - Core user flows (login, checkout, cart management)
   - Cross-browser compatibility
   - User type-specific behaviors

3. **User Acceptance Testing**:
   - Complete end-to-end scenarios
   - Edge case handling
   - Error condition validation

### Deployment Strategy

1. **Local Execution**:
   - Developer workstations with full browser installations
   - Batch files for easy execution

2. **CI/CD Integration**:
   - GitHub Actions workflow
   - Headless browser execution
   - Artifact generation and preservation

3. **Scaling Considerations**:
   - Parallel test execution configuration
   - Resource optimization for different environments
   - Cross-platform compatibility
