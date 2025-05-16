@echo off
echo ======================================================
echo Swag Labs E-commerce Test Automation - Parallel Tests
echo ======================================================
echo.

REM Set Java and Maven environment if needed
REM SET JAVA_HOME=C:\path\to\your\java
REM SET PATH=%JAVA_HOME%\bin;%PATH%

echo Running parallel tests...
call mvn clean test -Dtest=ParallelTestSuite
echo.

echo ======================================================
echo Parallel Test Execution Complete!
echo ======================================================
echo.
echo Test reports are available in:
echo target/surefire-reports
echo.
echo Screenshots of failed tests are in:
echo test-screenshots
echo.

pause
