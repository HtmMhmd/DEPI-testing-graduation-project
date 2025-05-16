@echo off
echo ======================================================
echo Swag Labs E-commerce Test Automation Suite
echo ======================================================
echo.

REM Set Java and Maven environment if needed
REM SET JAVA_HOME=C:\path\to\your\java
REM SET PATH=%JAVA_HOME%\bin;%PATH%

REM Check for command line arguments
set HEADLESS=false
set TEST_CLASS=AllTests

:PARSE_ARGS
if "%~1"=="" goto AFTER_ARGS
if /i "%~1"=="--headless" set HEADLESS=true
if /i "%~1"=="--test" set TEST_CLASS=%~2 & shift
shift
goto PARSE_ARGS
:AFTER_ARGS

echo Running %TEST_CLASS% tests with headless mode: %HEADLESS%...

REM Run the tests with the specified options
call mvn clean test -Dtest=%TEST_CLASS% -Dheadless=%HEADLESS%
echo.

echo ======================================================
echo Test Execution Complete!
echo ======================================================
echo.
echo Test reports are available in:
echo target/surefire-reports
echo.
echo Screenshots of failed tests are in:
echo test-screenshots
echo.

pause