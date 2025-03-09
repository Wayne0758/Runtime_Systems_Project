#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Analyze Java and Kotlin performance metrics
"""

import re
import sys
import numpy as np
import matplotlib.pyplot as plt
from collections import defaultdict

def analyze_performance(java_log_file, kotlin_log_file):
    """Analyze Java and Kotlin performance metrics"""
    
    # Read Java log file
    try:
        with open(java_log_file, 'r') as f:
            java_log_content = f.read()
    except Exception as e:
        print(f"Failed to read Java log file: {e}")
        return
    
    # Read Kotlin log file
    try:
        with open(kotlin_log_file, 'r') as f:
            kotlin_log_content = f.read()
    except Exception as e:
        print(f"Failed to read Kotlin log file: {e}")
        return
    
    # Extract Java warm-up data for different test types
    java_warmup_data = extract_java_warmup_data(java_log_content)
    
    # Extract Kotlin SimpleArithmetic data
    kotlin_simple_data = extract_kotlin_simple_data(kotlin_log_content)
    
    # Extract Kotlin test type data
    kotlin_test_data = extract_kotlin_test_data(kotlin_log_content)
    
    # Analyze and plot Java warm-up iterations for different test types
    if java_warmup_data:
        plot_java_warmup_iterations(java_warmup_data)
    else:
        print("No data found for Java with different warm-up iterations")
    
    # Plot Java vs Kotlin inline function execution time comparison
    if java_warmup_data and kotlin_test_data:
        plot_java_kotlin_comparison(java_warmup_data, kotlin_test_data)
    
    # Plot Kotlin inline function vs normal function performance comparison
    if kotlin_test_data:
        plot_kotlin_inline_vs_normal(kotlin_test_data)
    
    print("===== Analysis Complete =====")
    print("Analysis results:")
    print("- Java performance with different warm-up iterations: java_warmup_analysis.png")
    print("- Java inline vs Kotlin inline execution time: java_kotlin_inline_comparison.png")
    print("- Kotlin inline vs normal function performance: kotlin_performance_analysis.png")

def extract_java_warmup_data(log_content):
    """Extract Java warm-up data for different test types"""
    # Define test types and corresponding regex patterns
    test_patterns = {
        "Simple Arithmetic": r"=== Simple Arithmetic Function with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+",
        "Higher-Order Function": r"=== Higher-Order Function with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+",
        "Recursive Call": r"=== Recursive Call with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+",
        "Calls Within Loops": r"=== Calls Within Loops with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+",
        "Nested Call": r"=== Nested Call with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+",
        "Complex Condition": r"=== Complex Condition with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+",
        "Generic Function": r"=== Generic Function with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+"
    }
    
    # Store data for all test types
    all_test_data = {}
    
    # Extract data for each test type
    for test_type, pattern in test_patterns.items():
        match = re.search(pattern, log_content, re.DOTALL)
        if match:
            data_lines = match.group(1).strip().split('\n')
            warmup_iterations = []
            execution_times = []
            time_per_calls = []
            
            for line in data_lines:
                # Parse each line of data
                parts = line.split('|')
                if len(parts) >= 4:
                    try:
                        warmup_iter_str = parts[1].strip().replace(',', '')
                        exec_time_str = parts[2].strip().replace(',', '')
                        time_per_call_str = parts[3].strip().replace(',', '')
                        
                        # Ensure data is numeric
                        if not warmup_iter_str.replace('.', '', 1).isdigit():
                            continue
                        
                        warmup_iter = int(float(warmup_iter_str))
                        exec_time = float(exec_time_str)
                        time_per_call = float(time_per_call_str)
                        
                        warmup_iterations.append(warmup_iter)
                        execution_times.append(exec_time)
                        time_per_calls.append(time_per_call)
                    except (ValueError, IndexError) as e:
                        print(f"Parsing error: {e}, line: {line}")
                        continue
            
            if warmup_iterations:
                all_test_data[test_type] = {
                    "warmup_iterations": warmup_iterations,
                    "execution_times": execution_times,
                    "time_per_calls": time_per_calls
                }
    
    # Extract data from Combined Test Results for Java vs Kotlin comparison
    combined_pattern = r"=== Combined Test Results ===.*?\+\-+\+\-+\+\-+\+\n\|[^|]+\|[^|]+\|[^|]+\|\n\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+"
    match = re.search(combined_pattern, log_content, re.DOTALL)
    if match:
        data_lines = match.group(1).strip().split('\n')
        combined_data = {}
        
        for line in data_lines:
            parts = line.split('|')
            if len(parts) >= 4:
                try:
                    test_type = parts[1].strip()
                    exec_time_str = parts[2].strip().replace(',', '')
                    time_per_call_str = parts[3].strip().replace(',', '')
                    
                    exec_time = float(exec_time_str)
                    time_per_call = float(time_per_call_str)
                    
                    combined_data[test_type] = {
                        "execution_time": exec_time,
                        "time_per_call": time_per_call
                    }
                except (ValueError, IndexError) as e:
                    print(f"Parsing error in combined results: {e}, line: {line}")
                    continue
        
        all_test_data["combined"] = combined_data
    
    return all_test_data

def extract_kotlin_simple_data(log_content):
    """提取Kotlin SimpleArithmetic数据"""
    pattern = r"=== Simple Arithmetic Function with Different Warm-up Iterations ===.*?\+\-+\+\-+\+\-+\+\-+\+\n\| Warm-up Iterations\s+\| Inline Time \(ms\)\s+\| Normal Time \(ms\)\s+\| Difference \(%\)\s+\|\n\+\-+\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+\-+\+"
    
    match = re.search(pattern, log_content, re.DOTALL)
    if not match:
        return None
    
    data_lines = match.group(1).strip().split('\n')
    warmup_iterations = []
    inline_times = []
    normal_times = []
    time_diffs = []
    
    for line in data_lines:
        # Parse each line of data
        parts = line.split('|')
        if len(parts) >= 5:
            try:
                warmup_iter_str = parts[1].strip().replace(',', '')
                inline_time_str = parts[2].strip().replace(',', '')
                normal_time_str = parts[3].strip().replace(',', '')
                time_diff_str = parts[4].strip().replace(',', '')
                
                # Ensure data is numeric
                if not warmup_iter_str.replace('.', '', 1).isdigit():
                    continue
                
                warmup_iter = int(float(warmup_iter_str))
                inline_time = float(inline_time_str)
                normal_time = float(normal_time_str)
                time_diff = float(time_diff_str)
                
                warmup_iterations.append(warmup_iter)
                inline_times.append(inline_time)
                normal_times.append(normal_time)
                time_diffs.append(time_diff)
            except (ValueError, IndexError) as e:
                print(f"Parsing error: {e}, line: {line}")
                continue
    
    return {
        "warmup_iterations": warmup_iterations,
        "inline_times": inline_times,
        "normal_times": normal_times,
        "time_diffs": time_diffs
    }

def extract_kotlin_test_data(log_content):
    """Extract Kotlin test type data"""
    # Try to match new integrated table format
    pattern = r"=== Kotlin Inline Function Performance Summary ===.*?\+\-+\+\-+\+\-+\+\-+\+\n\| Test Type\s+\| Inline Time \(ms\)\s+\| Normal Time \(ms\)\s+\| Improvement \(%\)\s+\|\n\+\-+\+\-+\+\-+\+\-+\+\n(.*?)\+\-+\+\-+\+\-+\+\-+\+"
    
    match = re.search(pattern, log_content, re.DOTALL)
    if not match:
        # If no integrated table is found, try to match individual test results
        return extract_kotlin_individual_test_data(log_content)
    
    data_lines = match.group(1).strip().split('\n')
    test_data = {}
    
    for line in data_lines:
        # Parse each line of data
        parts = line.split('|')
        if len(parts) >= 5:
            try:
                test_type = parts[1].strip()
                inline_time_str = parts[2].strip().replace(',', '')
                normal_time_str = parts[3].strip().replace(',', '')
                improvement_str = parts[4].strip().replace(',', '')
                
                inline_time = float(inline_time_str)
                normal_time = float(normal_time_str)
                improvement = float(improvement_str)
                
                test_data[test_type] = {
                    "inline_time": inline_time,
                    "normal_time": normal_time,
                    "improvement": improvement
                }
            except (ValueError, IndexError) as e:
                print(f"Parsing error: {e}, line: {line}")
                continue
    
    return test_data

def extract_kotlin_individual_test_data(log_content):
    """Extract Kotlin individual test result data"""
    # Match individual test results
    pattern = r"\| Test Type\s+\| ([^|]+)\s+\|\n\+\-+\+\-+\+\-+\+\-+\+\n\| Execution Time \(ms\)\s+\| Function Type\s+\| Value\s+\|\n\+\-+\+\-+\+\-+\+\n\|\s+([0-9,.]+)\s+\| Inline Function\s+\|[^|]*\|\n\|\s+([0-9,.]+)\s+\| Normal Function\s+\|[^|]*\|\n\|\s+([0-9,.\-]+)%\s+\| Difference\s+\|[^|]*\|"
    
    matches = re.finditer(pattern, log_content, re.DOTALL)
    test_data = {}
    
    for match in matches:
        try:
            test_type = match.group(1).strip()
            inline_time = float(match.group(2).replace(',', ''))
            normal_time = float(match.group(3).replace(',', ''))
            improvement = float(match.group(4).replace(',', ''))
            
            test_data[test_type] = {
                "inline_time": inline_time,
                "normal_time": normal_time,
                "improvement": improvement
            }
        except Exception as e:
            print(f"Error parsing data for test: {e}")
            continue
    
    return test_data

def plot_java_warmup_iterations(java_warmup_data):
    """Plot Java warm-up iterations for different test types"""
    # Filter out combined data
    test_types = [t for t in java_warmup_data.keys() if t != "combined"]
    if not test_types:
        print("No test types found for Java warm-up iterations")
        return
    
    plt.figure(figsize=(15, 10))
    
    # Create subplot
    plt.subplot(2, 1, 1)
    
    # Plot execution time vs warm-up iterations for each test type
    for test_type in test_types:
        data = java_warmup_data[test_type]
        plt.plot(data["warmup_iterations"], data["execution_times"], 'o-', label=test_type)
    
    plt.title('Java Performance with Different Warm-up Iterations')
    plt.xlabel('Number of Warm-up Iterations')
    plt.ylabel('Execution Time (ms)')
    plt.grid(True)
    plt.legend()
    
    # Create subplot, show improvement percentage
    plt.subplot(2, 1, 2)
    
    # Calculate improvement percentage from no warm-up to max warm-up
    improvements = []
    test_type_names = []
    
    for test_type in test_types:
        data = java_warmup_data[test_type]
        if len(data["execution_times"]) >= 2:
            no_warmup = data["execution_times"][0]
            max_warmup = data["execution_times"][-1]
            improvement = (no_warmup - max_warmup) / no_warmup * 100
            improvements.append(improvement)
            test_type_names.append(test_type)
    
    # Plot improvement percentage bar chart
    colors = ['green' if imp > 0 else 'red' for imp in improvements]
    plt.bar(test_type_names, improvements, color=colors)
    plt.axhline(y=0, color='black', linestyle='-', alpha=0.3)
    
    plt.title('Performance Improvement from No Warm-up to Max Warm-up (%)')
    plt.xlabel('Test Type')
    plt.ylabel('Improvement (%)')
    plt.xticks(rotation=45, ha='right')
    
    plt.tight_layout()
    plt.savefig('java_warmup_analysis.png')
    print("Java performance with different warm-up iterations plot saved as 'java_warmup_analysis.png'")

def plot_java_kotlin_comparison(java_warmup_data, kotlin_test_data):
    """Plot Java vs Kotlin inline function execution time comparison"""
    if "combined" not in java_warmup_data:
        print("No combined data found for Java")
        return
    
    # Get common test types between Java and Kotlin
    java_combined = java_warmup_data["combined"]
    common_test_types = []
    
    # Map Java test type names to Kotlin test type names
    java_to_kotlin_mapping = {
        "Simple Arithmetic": "Simple Arithmetic",
        "High-Order Function": "Higher-Order Func",
        "Recursive Call": "Recursive Call",
        "Calls Within Loops": "Calls Within Loops",
        "Nested Call": "Nested Inline",
        "Complex Condition": "Complex Condition",
        "Generic Function": "Generic Function"
    }
    
    # Find common test types
    for java_type, kotlin_type in java_to_kotlin_mapping.items():
        if java_type in java_combined and kotlin_type in kotlin_test_data:
            common_test_types.append((java_type, kotlin_type))
    
    if not common_test_types:
        print("No common test types found between Java and Kotlin")
        return
    
    plt.figure(figsize=(12, 10))
    
    # Prepare data
    java_types = [java_type for java_type, _ in common_test_types]
    kotlin_types = [kotlin_type for _, kotlin_type in common_test_types]
    
    java_times = [java_combined[java_type]["execution_time"] for java_type, _ in common_test_types]
    kotlin_times = [kotlin_test_data[kotlin_type]["inline_time"] for _, kotlin_type in common_test_types]
    
    # Set bar chart position
    x = np.arange(len(common_test_types))
    width = 0.35
    
    # Plot execution time bar chart
    plt.subplot(2, 1, 1)
    plt.bar(x - width/2, java_times, width, label='Java')
    plt.bar(x + width/2, kotlin_times, width, label='Kotlin Inline')
    
    plt.title('Java vs Kotlin Inline Execution Time of Different Test Types')
    plt.xlabel('Test Type')
    plt.ylabel('Execution Time (ms)')
    plt.xticks(x, java_types, rotation=45, ha='right')
    plt.legend()
    
    # Calculate Kotlin performance improvement over Java
    improvements = []
    for i in range(len(common_test_types)):
        java_time = java_times[i]
        kotlin_time = kotlin_times[i]
        improvement = (java_time - kotlin_time) / java_time * 100
        improvements.append(improvement)
    
    # Plot performance improvement bar chart
    plt.subplot(2, 1, 2)
    colors = ['green' if imp > 0 else 'red' for imp in improvements]
    plt.bar(java_types, improvements, color=colors)
    
    plt.title('Kotlin Inline Performance Improvement over Java (%)')
    plt.xlabel('Test Type')
    plt.ylabel('Improvement (%)')
    plt.xticks(rotation=45, ha='right')
    plt.axhline(y=0, color='black', linestyle='-', alpha=0.3)
    
    plt.tight_layout()
    plt.savefig('java_kotlin_inline_comparison.png')
    print("Java inline vs Kotlin inline execution time comparison plot saved as 'java_kotlin_inline_comparison.png'")

def plot_kotlin_inline_vs_normal(test_data):
    """Plot Kotlin inline function vs normal function performance comparison"""
    plt.figure(figsize=(12, 10))
    
    # Prepare data
    test_types = list(test_data.keys())
    inline_times = [test_data[test_type]["inline_time"] for test_type in test_types]
    normal_times = [test_data[test_type]["normal_time"] for test_type in test_types]
    improvements = [test_data[test_type]["improvement"] for test_type in test_types]
    
    # Set bar chart position
    x = np.arange(len(test_types))
    width = 0.35
    
    # Plot execution time bar chart
    plt.subplot(2, 1, 1)
    plt.bar(x - width/2, inline_times, width, label='Inline Function')
    plt.bar(x + width/2, normal_times, width, label='Normal Function')
    
    plt.title('Kotlin Inline vs Normal Function Execution Time')
    plt.xlabel('Test Type')
    plt.ylabel('Execution Time (ms)')
    plt.xticks(x, test_types, rotation=45, ha='right')
    plt.legend()
    
    # Plot performance improvement bar chart
    plt.subplot(2, 1, 2)
    colors = ['green' if imp > 0 else 'red' for imp in improvements]
    plt.bar(test_types, improvements, color=colors)
    
    plt.title('Performance Improvement of Inline over Normal Function (%)')
    plt.xlabel('Test Type')
    plt.ylabel('Improvement (%)')
    plt.xticks(rotation=45, ha='right')
    plt.axhline(y=0, color='black', linestyle='-', alpha=0.3)
    
    plt.tight_layout()
    plt.savefig('kotlin_performance_analysis.png')
    print("Kotlin inline vs normal function performance analysis plot saved as 'kotlin_performance_analysis.png'")

def main():
    if len(sys.argv) < 3:
        print("Usage: python analyze_inline.py <java_log_file> <kotlin_log_file>")
        sys.exit(1)
    
    java_log_file = sys.argv[1]
    kotlin_log_file = sys.argv[2]
    
    analyze_performance(java_log_file, kotlin_log_file)

if __name__ == "__main__":
    main() 