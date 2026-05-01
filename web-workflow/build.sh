#!/bin/bash

# Build script for Workflow Engine Web Application
# This script builds the application and prepares it for deployment

set -e  # Exit on error

echo ""
echo "════════════════════════════════════════════════════════════"
echo "  🔄 OOP Workflow Engine - Build Script"
echo "════════════════════════════════════════════════════════════"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Get script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Check Java version
echo "🔍 Checking Java version..."
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[0-9]+' | head -1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo -e "${RED}❌ Java 17 or higher is required (found: Java $JAVA_VERSION)${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java $JAVA_VERSION found${NC}"

# Check Maven
echo "🔍 Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven is not installed${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven found$(mvn -v | head -1 | cut -d' ' -f3)${NC}"

echo ""
echo "📦 Building project..."
echo ""

cd "$SCRIPT_DIR"

# Build
if mvn clean package; then
    echo ""
    echo -e "${GREEN}✓ Build successful${NC}"
    JAR_FILE="target/workflow-engine-web-1.0.0.jar"
    if [ -f "$JAR_FILE" ]; then
        SIZE=$(du -h "$JAR_FILE" | cut -f1)
        echo -e "${GREEN}✓ JAR file created: $JAR_FILE ($SIZE)${NC}"
        echo ""
        echo "════════════════════════════════════════════════════════════"
        echo "  🎉 Build Complete!"
        echo "════════════════════════════════════════════════════════════"
        echo ""
        echo "To run the application:"
        echo ""
        echo -e "  ${YELLOW}java -jar $JAR_FILE${NC}"
        echo ""
        echo "Or use Maven:"
        echo ""
        echo -e "  ${YELLOW}mvn spring-boot:run${NC}"
        echo ""
        echo "Application will be available at:"
        echo ""
        echo -e "  ${YELLOW}http://localhost:8080${NC}"
        echo ""
    fi
else
    echo ""
    echo -e "${RED}❌ Build failed${NC}"
    exit 1
fi
