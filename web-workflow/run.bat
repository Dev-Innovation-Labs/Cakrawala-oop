@echo off
REM Quick Start Script for Workflow Engine Web Application (Windows)
REM Usage: run.bat

echo.
echo ════════════════════════════════════════════════════════
echo.  🔄 OOP Workflow Engine - Quick Start
echo ════════════════════════════════════════════════════════
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven is not installed. Please install Maven first.
    echo    Visit: https://maven.apache.org/download.cgi
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java is not installed. Please install Java 17+ first.
    exit /b 1
)

echo ✓ Java and Maven found
echo.

echo 📦 Building project...
call mvn clean package -q

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Build failed!
    exit /b 1
)

echo ✓ Build successful
echo.
echo 🚀 Starting application...
echo.
echo ════════════════════════════════════════════════════════
echo   Application starting on: http://localhost:8080
echo ════════════════════════════════════════════════════════
echo.

REM Run the application
call mvn spring-boot:run
