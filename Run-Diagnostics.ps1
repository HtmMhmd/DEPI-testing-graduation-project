Write-Host "======================================================"
Write-Host "Swag Labs Test Environment Diagnostics"
Write-Host "======================================================"
Write-Host ""

# Set working directory
Set-Location -Path "c:\Users\LENOVO\Desktop\New folder (2)"

Write-Host "Compiling the diagnostics utility..."
& mvn clean compile

Write-Host ""
Write-Host "Running environment diagnostics..."
& mvn exec:java -Dexec.mainClass="com.swaglabs.utils.TestEnvironmentDiagnostics"

Write-Host ""
Write-Host "======================================================"
Write-Host "Diagnostics Complete!"
Write-Host "======================================================"
Write-Host ""

# Wait for user input
Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
