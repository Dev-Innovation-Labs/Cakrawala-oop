#!/bin/bash

echo "════════════════════════════════════════════════════════"
echo "  Business Object Web Simulator - Quick Start"
echo "════════════════════════════════════════════════════════"
echo ""

if ! command -v mvn &> /dev/null && ! [ -x /usr/local/maven/bin/mvn ]; then
    echo "❌ Maven is not installed. Please install Maven first."
    exit 1
fi

if [ -x /usr/local/maven/bin/mvn ]; then
    MVN="/usr/local/maven/bin/mvn"
else
    MVN="mvn"
fi

if [ "$(uname)" = "Darwin" ]; then
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    export PATH="$JAVA_HOME/bin:$PATH"
fi

echo "Building and running Business Object Web Simulator..."
echo ""

$MVN spring-boot:run
