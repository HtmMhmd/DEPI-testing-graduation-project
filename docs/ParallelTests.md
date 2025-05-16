# Running Tests in Parallel

This document explains how to execute the Swag Labs test suite in parallel for faster test execution.

## Overview

Our test framework supports parallel test execution using JUnit 5's parallel execution capabilities and ThreadLocal WebDriver management. This allows multiple test cases to run simultaneously, significantly reducing the overall test execution time.

## Features

- **Parallel Test Execution**: Run multiple test classes concurrently
- **Thread-Safe WebDriver**: Each test thread has its own isolated WebDriver instance
- **Screenshot Capture**: Automatic screenshot capture for test failures
- **Configurable Thread Count**: Adjustable number of concurrent test threads

## How to Run Tests in Parallel

### Option 1: Using the Batch File

Simply run the provided `run_tests.bat` file:

```
cd project_directory
run_tests.bat
```

### Option 2: Using Maven Command

```
mvn clean test -Dtest=AllTests
```

## Advanced Configuration

### Thread Count

You can adjust the number of parallel threads by modifying the `pom.xml` file:

```xml
<configuration>
    <parallel>classes</parallel>
    <threadCount>3</threadCount>  <!-- Change this value -->
    <perCoreThreadCount>false</perCoreThreadCount>
</configuration>
```

### Running Specific Test Groups

To run only specific test groups:

```
mvn clean test -Dtest=LoginTests,ProductTests
```

## Understanding Test Output

When running tests in parallel, the output in the console may be interleaved. Test reports in the `target/surefire-reports` directory provide a clear separation of results for each test.

## Troubleshooting

### Common Issues

1. **Port Conflicts**: If you encounter connection refused errors, it's possible that multiple WebDriver instances are trying to use the same debugging port. Add `-Dwebdriver.chrome.driver.port=0` to the Maven command to use random ports.

2. **Resource Constraints**: Running too many browser instances simultaneously can exhaust system resources. Reduce the thread count if you experience performance issues.

3. **Failed Screenshots**: Ensure the `test-screenshots` directory is writeable.

## Best Practices

1. Keep tests independent to ensure they can run in any order
2. Avoid shared state between test classes
3. Clean up resources in `@AfterEach` methods
4. Use explicit waits rather than Thread.sleep() for synchronization
