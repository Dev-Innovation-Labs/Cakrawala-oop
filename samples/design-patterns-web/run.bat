@echo off
REM Design Patterns Web Simulator - Quick Start for Windows

echo.
echo ════════════════════════════════════════════════════════
echo   Design Patterns Web Simulator - Quick Start
echo ════════════════════════════════════════════════════════
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven is not installed. Please install Maven first.
    echo    Visit: https://maven.apache.org/download.cgi
    exit /b 1
)

echo Building and running Design Patterns Web Simulator...
echo.

call mvn spring-boot:run
