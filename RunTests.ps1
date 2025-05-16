Write-Host "======================================================"
Write-Host "Swag Labs E-commerce Test Automation Suite"
Write-Host "======================================================"
Write-Host ""

Write-Host "Running all tests..."
mvn clean test -Dtest=SimpleTest

Write-Host ""
Write-Host "======================================================"
Write-Host "Test Execution Complete!"
Write-Host "======================================================"
Write-Host ""
Write-Host "Test reports are available in:"
Write-Host "target/surefire-reports"
Write-Host ""
