@echo off
echo ======================================================
echo Swag Labs User Type Tests with Detailed Logging
echo ======================================================
echo.

set USER_TYPE=%1
if "%USER_TYPE%"=="" set USER_TYPE=all

if /i "%USER_TYPE%"=="all" (
    echo Running tests for ALL user types with detailed logging
    call mvn clean test -Dtest=UserTypeTests
) else if /i "%USER_TYPE%"=="standard" (
    echo Running tests for STANDARD_USER with detailed logging
    call mvn clean test -Dtest=UserTypeTests#testStandardUser
) else if /i "%USER_TYPE%"=="locked" (
    echo Running tests for LOCKED_OUT_USER with detailed logging
    call mvn clean test -Dtest=UserTypeTests#testLockedOutUser
) else if /i "%USER_TYPE%"=="problem" (
    echo Running tests for PROBLEM_USER with detailed logging
    call mvn clean test -Dtest=UserTypeTests#testProblemUser
) else if /i "%USER_TYPE%"=="glitch" (
    echo Running tests for PERFORMANCE_GLITCH_USER with detailed logging
    call mvn clean test -Dtest=UserTypeTests#testPerformanceGlitchUser
) else if /i "%USER_TYPE%"=="error" (
    echo Running tests for ERROR_USER with detailed logging
    call mvn clean test -Dtest=UserTypeTests#testErrorUser
) else if /i "%USER_TYPE%"=="visual" (
    echo Running tests for VISUAL_USER with detailed logging
    call mvn clean test -Dtest=UserTypeTests#testVisualUser
) else if /i "%USER_TYPE%"=="log" (
    echo Running comprehensive logging tests for ALL user types
    call mvn clean test -Dtest=UserTypeLogTests
) else (
    echo Unknown user type: %USER_TYPE%
    echo Valid options: all, standard, locked, problem, glitch, error, visual, log
    exit /b 1
)

echo.
echo ======================================================
echo Test execution complete!
echo ======================================================
echo.
echo Test logs are available in:
echo - logs/user-type-tests.log (All tests)
echo - logs/standard-user.log (Standard User)
echo - logs/locked-out-user.log (Locked Out User)
echo - logs/problem-user.log (Problem User)
echo - logs/performance-glitch-user.log (Performance Glitch User)
echo - logs/error-user.log (Error User)
echo - logs/visual-user.log (Visual User)
echo.
echo Test summary is available in:
echo - logs/test-summary.md
echo.

pause
