# Cross-Browser Testing Guide

This document provides guidance for conducting cross-browser testing with the Swag Labs test automation framework.

## Supported Browsers

The framework supports the following browsers:

1. **Google Chrome** - Default browser
2. **Mozilla Firefox**
3. **Microsoft Edge**
4. **Safari** - macOS only

## Running Cross-Browser Tests

### Command Line Options

The framework supports running tests on different browsers using the `browser` system property:

```
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
mvn clean test -Dbrowser=safari
```

### Using the Browser Test Scripts

#### Batch Script (Windows)

```
run_browser_tests.bat chrome firefox edge
```

Options:
- `--headless`: Run in headless mode
- `--test <TestClass>`: Specify a test class to run

Example:
```
run_browser_tests.bat --headless --test LoginTests chrome firefox
```

#### PowerShell Script (Windows)

```
.\RunBrowserTests.ps1 -Browsers "chrome,firefox,edge"
```

Parameters:
- `-Test`: Specific test class to run (default: AllTests)
- `-Headless`: Run in headless mode (default: false)
- `-Browsers`: Comma-separated list of browsers to test
- `-Parallel`: Run tests in parallel mode (default: false)

Example:
```
.\RunBrowserTests.ps1 -Test "LoginTests" -Browsers "chrome,firefox" -Headless $true
```

## Browser-Specific Considerations

### Chrome

- The most stable browser for automation
- Good support for headless mode
- Recommended for CI/CD pipelines

### Firefox

- Good compatibility with Selenium WebDriver
- Some minor differences in element rendering compared to Chrome
- Supports headless mode, but might be less stable than Chrome

### Edge

- Good compatibility with Chrome-based tests
- Supports headless mode
- May have issues with certain advanced Selenium features

### Safari

- Only available on macOS
- Does not support headless mode
- Requires enabling the "Allow Remote Automation" option in Safari's developer menu
- Most likely to have compatibility issues

## Best Practices

1. **Start with Chrome**: Develop and debug your tests with Chrome first
2. **Cross-browser testing frequency**: Run cross-browser tests periodically, not necessarily on every commit
3. **Handle browser differences**: Use robust element locators that work across browsers
4. **Different wait strategies**: Browser rendering speeds vary; adjust waits accordingly
5. **Screenshot comparisons**: Visual differences between browsers are normal

## Troubleshooting

### Firefox Issues

- If Firefox fails to launch, verify Firefox is installed and up-to-date
- Try using a specific Firefox version with WebDriverManager: `WebDriverManager.firefoxdriver().browserVersion("115").setup();`

### Edge Issues

- Edge WebDriver requires Microsoft Edge browser to be installed
- On Windows 7, Edge is not supported

### Safari Issues

- Safari WebDriver is included with macOS, no separate driver required
- Safari must have developer mode enabled:
  1. Open Safari
  2. Go to Safari > Preferences > Advanced
  3. Check "Show Develop menu in menu bar"
  4. Go to Develop > Allow Remote Automation

## CI/CD Integration

When running in CI/CD environments, use headless mode for all browsers:

```
mvn clean test -Dbrowser=chrome -Dheadless=true -Dci=true
```

This configuration works well with GitHub Actions, Jenkins, and other CI platforms.
