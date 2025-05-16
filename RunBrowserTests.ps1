# Swag Labs Browser Compatibility Test Runner
#
# This script runs tests across multiple browsers to ensure compatibility
# Options:
#   -Test : Specific test class to run (default: AllTests)
#   -Headless : Run in headless mode (default: false)
#   -Browsers : Comma-separated list of browsers to test (default: chrome,firefox,edge)
#   -Parallel : Run tests in parallel mode (default: false)
#
# Example usage:
#   .\RunBrowserTests.ps1 -Test LoginTests -Browsers chrome,firefox -Headless $true

param(
    [string]$Test = "AllTests",
    [bool]$Headless = $false,
    [string]$Browsers = "chrome",
    [bool]$Parallel = $false
)

# Set working directory
$workingDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location -Path $workingDirectory

function Write-ColorOutput($ForegroundColor) {
    # Save the current colors
    $prevForegroundColor = $host.UI.RawUI.ForegroundColor
    
    # Set the new foreground color
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    
    # Pass the remaining arguments to Write-Output
    if ($args) {
        Write-Output $args
    }
    
    # Restore the original colors
    $host.UI.RawUI.ForegroundColor = $prevForegroundColor
}

function Write-Header($message) {
    Write-ColorOutput Green "========================================================"
    Write-ColorOutput Green $message
    Write-ColorOutput Green "========================================================"
    Write-Output ""
}

function Run-TestsWithBrowser($browser) {
    Write-Header "Running tests with $browser browser"

    # Create the Maven command
    $mvnCommand = "mvn clean test"
    $mvnCommand += " -Dbrowser=$browser"
    $mvnCommand += " -Dtest=$Test"
    
    if ($Headless) {
        $mvnCommand += " -Dheadless=true"
        Write-Output "Headless mode: Enabled"
    } else {
        Write-Output "Headless mode: Disabled"
    }
    
    if ($Parallel) {
        $mvnCommand += " -Dparallel=classes -DthreadCount=3"
        Write-Output "Parallel execution: Enabled"
    } else {
        Write-Output "Parallel execution: Disabled"
    }
    
    Write-Output "Command: $mvnCommand"
    Write-Output ""
    
    # Execute the command
    Invoke-Expression $mvnCommand
    
    Write-Output ""
    Write-ColorOutput Cyan "Tests completed for $browser browser"
    Write-Output ""
}

# Main script execution
Write-Header "Swag Labs Browser Compatibility Test Runner"

$browserList = $Browsers -split ","
foreach ($browser in $browserList) {
    $browser = $browser.Trim().ToLower()
    Run-TestsWithBrowser $browser
}

Write-Header "All browser tests completed!"
Write-Output "Test reports are available in target/surefire-reports"
Write-Output "Screenshots of failed tests are in test-screenshots"
