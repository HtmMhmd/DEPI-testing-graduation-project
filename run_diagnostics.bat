@echo off
echo ======================================================
echo Swag Labs Test Environment Diagnostics
echo ======================================================
echo.

REM Set Java and Maven environment if needed
REM SET JAVA_HOME=C:\path\to\your\java
REM SET PATH=%JAVA_HOME%\bin;%PATH%

echo Compiling the diagnostics utility...
call mvn clean compile

echo.
echo Running environment diagnostics...
call mvn exec:java -Dexec.mainClass="com.swaglabs.utils.TestEnvironmentDiagnostics"

echo.
echo ======================================================
echo Diagnostics Complete!
echo ======================================================
echo.
pause
