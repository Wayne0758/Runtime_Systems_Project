#!/bin/bash

# create build directory
mkdir -p ../build/classes

echo "===== Compile Inline Benchmark ====="

echo "Starting Java Inline Benchmark Compile..."
javac -d ../build/classes JavaBenchmark.java

echo "Starting Kotlin Inline Benchmark Compile..."
kotlinc -d ../build/classes InlineBenchmark.kt

# get kotlin stdlib path
KOTLIN_STDLIB=$(find /opt/homebrew/Cellar/kotlin -name "kotlin-stdlib.jar" | head -1)
if [ -z "$KOTLIN_STDLIB" ]; then
    echo "Error: Kotlin stdlib not found, please ensure Kotlin is installed"
    exit 1
fi

echo "===== Run Inline Benchmark ====="

echo "Starting Java Inline Benchmark..."
# Run Java tests and save output to log file
java -cp ../build/classes JavaBenchmark > java_performance_log.txt 2>&1

echo "Starting Kotlin Inline Benchmark..."
# Run Kotlin tests and save output to log file
java -cp ../build/classes:$KOTLIN_STDLIB InlineBenchmarkKt > kotlin_performance_log.txt 2>&1

echo "===== All compilation tests completed ====="

# Ensure Python scripts have execution permissions
chmod +x analyze_inline.py

# Install necessary Python dependencies
pip install numpy matplotlib

# Run the combined analysis script
echo "===== Running Performance Analysis ====="
python3 analyze_inline.py java_performance_log.txt kotlin_performance_log.txt

echo "===== Analysis Complete ====="
echo "Analysis results:"
echo "- Java performance with different warm-up iterations: java_warmup_analysis.png"
echo "- Java inline vs Kotlin inline execution time: java_kotlin_inline_comparison.png"
echo "- Kotlin inline vs normal function performance: kotlin_performance_analysis.png" 