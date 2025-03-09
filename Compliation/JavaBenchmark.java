/**
 * Java JIT Auto-Inlining Benchmark
 * Testing JIT auto-inlining performance in different scenarios
 */
import java.util.ArrayList;
import java.util.List;

public class JavaBenchmark {
    private static final int ITERATIONS = 10_000_000;
    private static final int WARMUP_BATCH_SIZE = 500_000; // Batch size for each warm-up
    private static final int DEFAULT_WARMUP_ITERATIONS = 2; // Default warm-up iterations for other functions
    
    // Add a list to store test results
    private static class TestResult {
        String testType;
        double elapsedTimeMs;
        double timePerCallNs;
        
        TestResult(String testType, double elapsedTimeMs, double timePerCallNs) {
            this.testType = testType;
            this.elapsedTimeMs = elapsedTimeMs;
            this.timePerCallNs = timePerCallNs;
        }
    }
    
    private List<TestResult> testResults = new ArrayList<>();
    
    // Different warm-up iterations to test for all test types
    private static final int[] WARMUP_ITERATIONS = {0, 1, 2, 5, 10};
    
    // Simple arithmetic test with different warm-up iterations
    public void runSimpleArithmeticTestsWithWarmup() {
        System.out.println("\n=== Simple Arithmetic Function with Different Warm-up Iterations ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Warm-up Iterations   | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (int warmupCount : WARMUP_ITERATIONS) {
            // Perform warm-up
            if (warmupCount > 0) {
                for (int iteration = 1; iteration <= warmupCount; iteration++) {
                    // Force GC for more accurate measurements
                    System.gc();
                    
                    int result = 0;
                    for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                        result += javaAdd(i, i + 1);
                    }
                }
            }
            
            // Measure actual performance after warm-up
            int result = 0;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < ITERATIONS; i++) {
                result += javaAdd(i, i + 1);
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;
            double timePerCallNs = (double) elapsedTime / ITERATIONS;
            
            System.out.printf("| %,20d | %,20.2f | %,20.2f |\n", 
                    warmupCount, elapsedTimeMs, timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Higher-order function test with different warm-up iterations
    public void runHigherOrderFunctionTestsWithWarmup() {
        System.out.println("\n=== Higher-Order Function with Different Warm-up Iterations ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Warm-up Iterations   | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (int warmupCount : WARMUP_ITERATIONS) {
            // Perform warm-up
            if (warmupCount > 0) {
                for (int iteration = 1; iteration <= warmupCount; iteration++) {
                    // Force GC for more accurate measurements
                    System.gc();
                    
                    int result = 0;
                    for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                        result += genericOperation(i, i + 1, (a, b) -> a + b);
                    }
                }
            }
            
            // Measure actual performance after warm-up
            int result = 0;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < ITERATIONS; i++) {
                result += genericOperation(i, i + 1, (a, b) -> a + b);
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;
            double timePerCallNs = (double) elapsedTime / ITERATIONS;
            
            System.out.printf("| %,20d | %,20.2f | %,20.2f |\n", 
                    warmupCount, elapsedTimeMs, timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Recursive call test with different warm-up iterations
    public void runRecursiveCallTestsWithWarmup() {
        System.out.println("\n=== Recursive Call with Different Warm-up Iterations ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Warm-up Iterations   | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (int warmupCount : WARMUP_ITERATIONS) {
            // Perform warm-up
            if (warmupCount > 0) {
                for (int iteration = 1; iteration <= warmupCount; iteration++) {
                    // Force GC for more accurate measurements
                    System.gc();
                    
                    long result = 0;
                    for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                        result += factorial(5);
                    }
                }
            }
            
            // Measure actual performance after warm-up
            long result = 0;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < ITERATIONS; i++) {
                result += factorial(5); // Use a small value to avoid stack overflow
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;
            double timePerCallNs = (double) elapsedTime / ITERATIONS;
            
            System.out.printf("| %,20d | %,20.2f | %,20.2f |\n", 
                    warmupCount, elapsedTimeMs, timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Loop call test with different warm-up iterations
    public void runLoopCallTestsWithWarmup() {
        System.out.println("\n=== Calls Within Loops with Different Warm-up Iterations ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Warm-up Iterations   | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (int warmupCount : WARMUP_ITERATIONS) {
            // Perform warm-up
            if (warmupCount > 0) {
                for (int iteration = 1; iteration <= warmupCount; iteration++) {
                    // Force GC for more accurate measurements
                    System.gc();
                    
                    int result = 0;
                    for (int i = 0; i < WARMUP_BATCH_SIZE / 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            result += javaAdd(i, j);
                        }
                    }
                }
            }
            
            // Measure actual performance after warm-up
            int result = 0;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < ITERATIONS / 10; i++) {
                for (int j = 0; j < 10; j++) {
                    result += javaAdd(i, j);
                }
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;
            double timePerCallNs = (double) elapsedTime / ITERATIONS;
            
            System.out.printf("| %,20d | %,20.2f | %,20.2f |\n", 
                    warmupCount, elapsedTimeMs, timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Nested call test with different warm-up iterations
    public void runNestedCallTestsWithWarmup() {
        System.out.println("\n=== Nested Call with Different Warm-up Iterations ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Warm-up Iterations   | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (int warmupCount : WARMUP_ITERATIONS) {
            // Perform warm-up
            if (warmupCount > 0) {
                for (int iteration = 1; iteration <= warmupCount; iteration++) {
                    // Force GC for more accurate measurements
                    System.gc();
                    
                    int result = 0;
                    for (int i = 0; i < WARMUP_BATCH_SIZE / 10; i++) {
                        result += nestedOperation(i, i + 1, i + 2);
                    }
                }
            }
            
            // Measure actual performance after warm-up
            int result = 0;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < ITERATIONS / 10; i++) {
                result += nestedOperation(i, i + 1, i + 2);
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;
            double timePerCallNs = (double) elapsedTime / (ITERATIONS / 10);
            
            System.out.printf("| %,20d | %,20.2f | %,20.2f |\n", 
                    warmupCount, elapsedTimeMs, timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Complex condition test with different warm-up iterations
    public void runComplexConditionTestsWithWarmup() {
        System.out.println("\n=== Complex Condition with Different Warm-up Iterations ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Warm-up Iterations   | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (int warmupCount : WARMUP_ITERATIONS) {
            // Perform warm-up
            if (warmupCount > 0) {
                for (int iteration = 1; iteration <= warmupCount; iteration++) {
                    // Force GC for more accurate measurements
                    System.gc();
                    
                    int result = 0;
                    for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                        result += complexCondition(i);
                    }
                }
            }
            
            // Measure actual performance after warm-up
            int result = 0;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < ITERATIONS; i++) {
                result += complexCondition(i);
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;
            double timePerCallNs = (double) elapsedTime / ITERATIONS;
            
            System.out.printf("| %,20d | %,20.2f | %,20.2f |\n", 
                    warmupCount, elapsedTimeMs, timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Generic function test with different warm-up iterations
    public void runGenericFunctionTestsWithWarmup() {
        System.out.println("\n=== Generic Function with Different Warm-up Iterations ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Warm-up Iterations   | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (int warmupCount : WARMUP_ITERATIONS) {
            // Perform warm-up
            if (warmupCount > 0) {
                for (int iteration = 1; iteration <= warmupCount; iteration++) {
                    // Force GC for more accurate measurements
                    System.gc();
                    
                    Integer result = 0;
                    for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                        result = genericOperation(i, i + 1, (a, b) -> a + b);
                    }
                }
            }
            
            // Measure actual performance after warm-up
            Integer result = 0;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < ITERATIONS; i++) {
                result = genericOperation(i, i + 1, (a, b) -> a + b);
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeMs = elapsedTime / 1_000_000.0;
            double timePerCallNs = (double) elapsedTime / ITERATIONS;
            
            System.out.printf("| %,20d | %,20.2f | %,20.2f |\n", 
                    warmupCount, elapsedTimeMs, timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Simple arithmetic test with default warm-up
    public void runSimpleArithmeticTest() {
        // Perform fixed warm-up
        for (int iteration = 1; iteration <= DEFAULT_WARMUP_ITERATIONS; iteration++) {
            int result = 0;
            for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                result += javaAdd(i, i + 1);
            }
        }
        
        int result = 0;
        
        // Measure execution time
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            result += javaAdd(i, i + 1);
        }
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        double timePerCallNs = (double) elapsedTime / ITERATIONS;
        
        printDetailedResult("Simple Arithmetic", elapsedTimeMs, timePerCallNs);
    }
    
    // Higher-order function test with default warm-up
    public void runHigherOrderFunctionTest() {
        // Perform fixed warm-up
        for (int iteration = 1; iteration <= DEFAULT_WARMUP_ITERATIONS; iteration++) {
            int result = 0;
            for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                result += genericOperation(i, i + 1, (a, b) -> a + b);
            }
        }
        
        int result = 0;
        
        // Measure execution time
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            result += genericOperation(i, i + 1, (a, b) -> a + b);
        }
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        double timePerCallNs = (double) elapsedTime / ITERATIONS;
        
        printDetailedResult("High-Order Function", elapsedTimeMs, timePerCallNs);
    }
    
    // Recursive call test with default warm-up
    public void runRecursiveCallTest() {
        // Perform fixed warm-up
        for (int iteration = 1; iteration <= DEFAULT_WARMUP_ITERATIONS; iteration++) {
            long result = 0;
            for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                result += factorial(5);
            }
        }
        
        long result = 0;
        
        // Measure execution time
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            result += factorial(5); // Use a small value to avoid stack overflow
        }
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        double timePerCallNs = (double) elapsedTime / ITERATIONS;
        
        printDetailedResult("Recursive Call", elapsedTimeMs, timePerCallNs);
    }
    
    // Loop call test with default warm-up
    public void runLoopCallTest() {
        // Perform fixed warm-up
        for (int iteration = 1; iteration <= DEFAULT_WARMUP_ITERATIONS; iteration++) {
            int result = 0;
            for (int i = 0; i < WARMUP_BATCH_SIZE / 10; i++) {
                for (int j = 0; j < 10; j++) {
                    result += javaAdd(i, j);
                }
            }
        }
        
        int result = 0;
        
        // Measure execution time
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS / 10; i++) {
            for (int j = 0; j < 10; j++) {
                result += javaAdd(i, j);
            }
        }
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        double timePerCallNs = (double) elapsedTime / ITERATIONS;
        
        printDetailedResult("Calls Within Loops", elapsedTimeMs, timePerCallNs);
    }
    
    // Nested call test with default warm-up
    public void runNestedCallTest() {
        // Perform fixed warm-up
        for (int iteration = 1; iteration <= DEFAULT_WARMUP_ITERATIONS; iteration++) {
            int result = 0;
            for (int i = 0; i < WARMUP_BATCH_SIZE / 10; i++) {
                result += nestedOperation(i, i + 1, i + 2);
            }
        }
        
        int result = 0;
        
        // Measure execution time
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS / 10; i++) {
            result += nestedOperation(i, i + 1, i + 2);
        }
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        double timePerCallNs = (double) elapsedTime / (ITERATIONS / 10);
        
        printDetailedResult("Nested Call", elapsedTimeMs, timePerCallNs);
    }
    
    // Complex condition test with default warm-up
    public void runComplexConditionTest() {
        // Perform fixed warm-up
        for (int iteration = 1; iteration <= DEFAULT_WARMUP_ITERATIONS; iteration++) {
            int result = 0;
            for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                result += complexCondition(i);
            }
        }
        
        int result = 0;
        
        // Measure execution time
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            result += complexCondition(i);
        }
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        double timePerCallNs = (double) elapsedTime / ITERATIONS;
        
        printDetailedResult("Complex Condition", elapsedTimeMs, timePerCallNs);
    }
    
    // Generic function test with default warm-up
    public void runGenericFunctionTest() {
        // Perform fixed warm-up
        for (int iteration = 1; iteration <= DEFAULT_WARMUP_ITERATIONS; iteration++) {
            Integer result = 0;
            for (int i = 0; i < WARMUP_BATCH_SIZE; i++) {
                result = genericOperation(i, i + 1, (a, b) -> a + b);
            }
        }
        
        Integer result = 0;
        
        // Measure execution time
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            result = genericOperation(i, i + 1, (a, b) -> a + b);
        }
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeMs = elapsedTime / 1_000_000.0;
        double timePerCallNs = (double) elapsedTime / ITERATIONS;
        
        printDetailedResult("Generic Function", elapsedTimeMs, timePerCallNs);
    }
    
    // Print detailed result
    private void printDetailedResult(String testType, double elapsedTimeMs, double timePerCallNs) {
        // Store the result for later combined output
        testResults.add(new TestResult(testType, elapsedTimeMs, timePerCallNs));
    }
    
    // Print combined results table
    private void printCombinedResults() {
        if (testResults.isEmpty()) {
            return;
        }
        
        System.out.println("\n=== Combined Test Results ===");
        System.out.println("+----------------------+----------------------+----------------------+");
        System.out.println("| Test Type            | Execution Time (ms)  | Time Per Call (ns)   |");
        System.out.println("+----------------------+----------------------+----------------------+");
        
        for (TestResult result : testResults) {
            System.out.printf("| %-20s | %,20.2f | %,20.2f |\n", 
                    result.testType, result.elapsedTimeMs, result.timePerCallNs);
        }
        
        System.out.println("+----------------------+----------------------+----------------------+");
    }
    
    // Simple function definition
    public static int javaAdd(int a, int b) {
        return a + b;
    }
    
    // Recursive function definition
    public static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
    
    // Nested function definition
    public static int nestedOperation(int a, int b, int c) {
        return javaAdd(javaAdd(a, b), c);
    }
    
    // Complex condition function
    public static int complexCondition(int value) {
        if (value % 2 == 0) {
            if (value % 3 == 0) {
                return value * 2;
            } else if (value % 5 == 0) {
                return value * 3;
            } else {
                return value + 1;
            }
        } else {
            if (value % 3 == 0) {
                return value / 3;
            } else if (value % 5 == 0) {
                return value / 5;
            } else {
                return value - 1;
            }
        }
    }
    
    // Generic function definition
    public static <T> T genericOperation(T a, T b, GenericOperation<T> operation) {
        return operation.apply(a, b);
    }
    
    // Generic function interface definition
    @FunctionalInterface
    interface GenericOperation<T> {
        T apply(T a, T b);
    }
    
    public static void main(String[] args) {
        JavaBenchmark benchmark = new JavaBenchmark();
        
        System.out.println("Java JIT Auto-Inlining Benchmark:");
        System.out.println("==================================");
        
        // Run tests with different warm-up iterations
        benchmark.runSimpleArithmeticTestsWithWarmup();
        benchmark.runHigherOrderFunctionTestsWithWarmup();
        benchmark.runRecursiveCallTestsWithWarmup();
        benchmark.runLoopCallTestsWithWarmup();
        benchmark.runNestedCallTestsWithWarmup();
        benchmark.runComplexConditionTestsWithWarmup();
        benchmark.runGenericFunctionTestsWithWarmup();
        
        // Run standard tests with fixed warm-up
        benchmark.runSimpleArithmeticTest();
        benchmark.runHigherOrderFunctionTest();
        benchmark.runRecursiveCallTest();
        benchmark.runLoopCallTest();
        benchmark.runNestedCallTest();
        benchmark.runComplexConditionTest();
        benchmark.runGenericFunctionTest();
        
        // Print combined results table
        benchmark.printCombinedResults();
        
        System.out.println("==================================");
        System.out.println("Java JIT Auto-Inlining Benchmark Completed");
    }
}
