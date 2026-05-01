#!/bin/bash

# Quick Start Script for Workflow Engine Web Application
# Usage: ./run.sh

echo "════════════════════════════════════════════════════════"
echo "  🔄 OOP Workflow Engine - Quick Start"
echo "════════════════════════════════════════════════════════"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven first."
    echo "   Visit: https://maven.apache.org/download.cgi"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17+ first."
    exit 1
fi

echo "✓ Java and Maven found"
echo ""

# Navigate to project directory
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo "📦 Building project..."
mvn clean package -q

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✓ Build successful"
echo ""
echo "🚀 Starting application..."
echo ""
echo "════════════════════════════════════════════════════════"
echo "  Application starting on: http://localhost:8080"
echo "════════════════════════════════════════════════════════"
echo ""

# Run the application
mvn spring-boot:run
