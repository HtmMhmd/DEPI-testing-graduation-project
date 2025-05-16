@echo off
REM Swag Labs Browser Compatibility Test Runner
REM
REM This script runs tests across multiple browsers to ensure compatibility
REM
REM Example usage:
REM   run_browser_tests.bat chrome firefox
REM   run_browser_tests.bat --headless chrome
REM   run_browser_tests.bat --test LoginTests --headless chrome firefox edge

echo ======================================================
echo Swag Labs Browser Compatibility Test Runner
echo ======================================================
echo.

REM Default values
set HEADLESS=false
set TEST_CLASS=AllTests

:PARSE_ARGS
if "%~1"=="" goto RUN_TESTS
if /i "%~1"=="--headless" (
    set HEADLESS=true
    shift
    goto PARSE_ARGS
)
if /i "%~1"=="--test" (
    set TEST_CLASS=%~2
    shift
    shift
    goto PARSE_ARGS
)
if /i "%~1"=="--help" (
    goto SHOW_HELP
)

set BROWSERS=%BROWSERS% %1
shift
goto PARSE_ARGS

:SHOW_HELP
echo Usage: run_browser_tests.bat [options] browser1 [browser2] [browser3]
echo.
echo Options:
echo   --headless       Run in headless mode
echo   --test ^<class^>   Specify test class to run (default: AllTests)
echo   --help           Show this help message
echo.
echo Browsers:
echo   chrome           Run tests with Chrome browser
echo   firefox          Run tests with Firefox browser
echo   edge             Run tests with Edge browser
echo.
echo Examples:
echo   run_browser_tests.bat chrome
echo   run_browser_tests.bat --headless chrome firefox
echo   run_browser_tests.bat --test LoginTests chrome edge
echo.
goto :EOF

:RUN_TESTS
REM If no browsers specified, use Chrome as default
if "%BROWSERS%"=="" set BROWSERS=chrome

echo Settings:
echo - Test class: %TEST_CLASS%
echo - Headless mode: %HEADLESS%
echo - Browsers: %BROWSERS%
echo.

REM Run tests for each browser
for %%b in (%BROWSERS%) do (
    echo ======================================================
    echo Running tests with %%b browser
    echo ======================================================
    
    call mvn clean test -Dbrowser=%%b -Dtest=%TEST_CLASS% -Dheadless=%HEADLESS%
    
    echo.
    echo Tests completed for %%b browser
    echo.
)

echo ======================================================
echo All browser tests completed!
echo ======================================================
echo.
echo Test reports are available in:
echo target/surefire-reports
echo.
echo Screenshots of failed tests are in:
echo test-screenshots
echo.

pause
