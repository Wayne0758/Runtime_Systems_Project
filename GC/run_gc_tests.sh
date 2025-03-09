#!/bin/bash

# create build directory
mkdir -p ../build/classes

echo "===== Compile GC Test ====="

echo "Starting Java GC Test Compile..."
javac -d ../build/classes GCTest.java

echo "Starting Kotlin GC Test Compile..."
kotlinc -d ../build/classes GCTest.kt

echo "===== Compile NPE Test ====="

echo "Starting Java NPE Test Compile..."
javac -d ../build/classes NpeTest.java

echo "Starting Kotlin NPE Test Compile..."
kotlinc -d ../build/classes NpeTestForced.kt
kotlinc -d ../build/classes NpeTestSafe.kt

# get kotlin stdlib path
KOTLIN_STDLIB=$(find /opt/homebrew/Cellar/kotlin -name "kotlin-stdlib.jar" | head -1)
if [ -z "$KOTLIN_STDLIB" ]; then
    echo "Error: Kotlin stdlib not found, please ensure Kotlin is installed"
    exit 1
fi

echo "===== Run GC Test ====="

echo "Starting Java GC Test..."
java -cp ../build/classes GCTest

echo "Starting Kotlin GC Test..."
java -cp ../build/classes:$KOTLIN_STDLIB GCTestKt

echo "===== Run NPE Test ====="

echo "Starting Java NPE Test..."
java -cp ../build/classes NpeTest

echo "Starting Kotlin NPE Test (use !!)..."
java -cp ../build/classes:$KOTLIN_STDLIB NpeTestForcedKt

echo "Starting Kotlin NPE (use null safety)..."
java -cp ../build/classes:$KOTLIN_STDLIB NpeTestSafeKt

echo "===== All GC tests completed =====" 