@echo off
echo.
echo ════════════════════════════════════════════════════════
echo   Business Object Web Simulator - Quick Start
echo ════════════════════════════════════════════════════════
echo.

where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven is not installed.
    exit /b 1
)

echo Building and running Business Object Web Simulator...
echo.

call mvn spring-boot:run
