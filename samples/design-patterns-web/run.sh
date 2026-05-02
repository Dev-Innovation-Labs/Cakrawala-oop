#!/bin/bash

echo "════════════════════════════════════════════════════════"
echo "  Design Patterns Web Simulator - Quick Start"
echo "════════════════════════════════════════════════════════"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null && ! [ -x /usr/local/maven/bin/mvn ]; then
    echo "❌ Maven is not installed. Please install Maven first."
    echo "   Visit: https://maven.apache.org/download.cgi"
    exit 1
fi

# Get Maven path
if [ -x /usr/local/maven/bin/mvn ]; then
    MVN="/usr/local/maven/bin/mvn"
else
    MVN="mvn"
fi

# Set Java 17 for macOS
if [ "$(uname)" = "Darwin" ]; then
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    export PATH="$JAVA_HOME/bin:$PATH"
fi

echo "Building and running Design Patterns Web Simulator..."
echo ""

$MVN spring-boot:run
