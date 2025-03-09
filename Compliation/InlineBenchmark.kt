import kotlin.system.measureNanoTime

/**
 * Kotlin Inline Function Benchmark
 * Testing inline function performance in different scenarios
 */
class InlineBenchmark {
    private val iterations = 10_000_000
    
    // Store test results
    private data class TestResult(
        val testType: String,
        val inlineTimeMs: Double,
        val normalTimeMs: Double,
        val improvement: Double
    )
    
    private val testResults = mutableListOf<TestResult>()
    
    // Simple arithmetic test
    fun runSimpleArithmeticTest() {
        var inlineResult = 0
        var normalResult = 0
        
        // directly measure inline function performance
        val inlineTime = measureNanoTime {
            for (i in 0 until iterations) {
                inlineResult += inlineAdd(i, i + 1)
            }
        }
        
        System.gc()
        
        // measure normal function performance
        val normalTime = measureNanoTime {
            for (i in 0 until iterations) {
                normalResult += normalAdd(i, i + 1)
            }
        }
        
        val inlineTimeMs = inlineTime / 1_000_000.0
        val normalTimeMs = normalTime / 1_000_000.0
        val improvement = normalTime.toDouble() / inlineTime.toDouble() * 100 - 100

        testResults.add(TestResult("Simple Arithmetic", inlineTimeMs, normalTimeMs, improvement))
    }
    
    // Higher-order function test
    fun runHigherOrderFunctionTest() {
        var inlineResult = 0
        var normalResult = 0
        
        // directly measure inline function performance
        val inlineTime = measureNanoTime {
            for (i in 0 until iterations) {
                inlineResult += inlineGenericOperation(i, i + 1) { a, b -> a + b }
            }
        }
        
        System.gc()
        
        // measure normal function performance
        val normalTime = measureNanoTime {
            for (i in 0 until iterations) {
                normalResult += normalGenericOperation(i, i + 1) { a, b -> a + b }
            }
        }
        
        val inlineTimeMs = inlineTime / 1_000_000.0
        val normalTimeMs = normalTime / 1_000_000.0
        val improvement = normalTime.toDouble() / inlineTime.toDouble() * 100 - 100

        testResults.add(TestResult("Higher-Order Func", inlineTimeMs, normalTimeMs, improvement))
    }
    
    // Loop call test
    fun runLoopCallTest() {
        var inlineResult = 0
        var normalResult = 0
        
        // directly measure inline function performance
        val inlineTime = measureNanoTime {
            for (i in 0 until iterations / 10) {
                for (j in 0 until 10) {
                    inlineResult += inlineAdd(i, j)
                }
            }
        }
        
        System.gc()
        
        // measure normal function performance
        val normalTime = measureNanoTime {
            for (i in 0 until iterations / 10) {
                for (j in 0 until 10) {
                    normalResult += normalAdd(i, j)
                }
            }
        }
        
        val inlineTimeMs = inlineTime / 1_000_000.0
        val normalTimeMs = normalTime / 1_000_000.0
        val improvement = normalTime.toDouble() / inlineTime.toDouble() * 100 - 100
        

        testResults.add(TestResult("Calls Within Loops", inlineTimeMs, normalTimeMs, improvement))
    }
    
    // Nested inline function test
    fun runNestedInlineTest() {
        var inlineResult = 0
        var normalResult = 0
        
        // directly measure inline function performance
        val inlineTime = measureNanoTime {
            for (i in 0 until iterations / 10) {
                inlineResult += inlineNestedOperation(i, i + 1, i + 2)
            }
        }
        
        System.gc()
        
        // measure normal function performance
        val normalTime = measureNanoTime {
            for (i in 0 until iterations / 10) {
                normalResult += normalNestedOperation(i, i + 1, i + 2)
            }
        }
        
        val inlineTimeMs = inlineTime / 1_000_000.0
        val normalTimeMs = normalTime / 1_000_000.0
        val improvement = normalTime.toDouble() / inlineTime.toDouble() * 100 - 100
        
        testResults.add(TestResult("Nested Inline", inlineTimeMs, normalTimeMs, improvement))
    }
    
    // Complex condition branch inline test
    fun runComplexConditionTest() {
        var inlineResult = 0
        var normalResult = 0
        
        // directly measure inline function performance
        val inlineTime = measureNanoTime {
            for (i in 0 until iterations) {
                inlineResult += inlineComplexCondition(i)
            }
        }
        
        System.gc()
        
        // measure normal function performance
        val normalTime = measureNanoTime {
            for (i in 0 until iterations) {
                normalResult += normalComplexCondition(i)
            }
        }
        
        val inlineTimeMs = inlineTime / 1_000_000.0
        val normalTimeMs = normalTime / 1_000_000.0
        val improvement = normalTime.toDouble() / inlineTime.toDouble() * 100 - 100
        
        testResults.add(TestResult("Complex Condition", inlineTimeMs, normalTimeMs, improvement))
    }
    
    // Generic function inline test
    fun runGenericFunctionTest() {
        var inlineResult = 0
        var normalResult = 0
        
        // directly measure inline function performance
        val inlineTime = measureNanoTime {
            for (i in 0 until iterations) {
                inlineResult += inlineGenericOperation(i, i + 1, Int::plus)
            }
        }
        
        System.gc()
        
        // measure normal function performance
        val normalTime = measureNanoTime {
            for (i in 0 until iterations) {
                normalResult += normalGenericOperation(i, i + 1, Int::plus)
            }
        }
        
        val inlineTimeMs = inlineTime / 1_000_000.0
        val normalTimeMs = normalTime / 1_000_000.0
        val improvement = normalTime.toDouble() / inlineTime.toDouble() * 100 - 100
        
        testResults.add(TestResult("Generic Function", inlineTimeMs, normalTimeMs, improvement))
    }
    
    // print combined results table
    fun printResults() {
        println("\n=== Kotlin Inline Function Performance Summary ===")
        println("+----------------------+----------------------+----------------------+----------------------+")
        println("| Test Type            | Inline Time (ms)     | Normal Time (ms)     | Improvement (%)      |")
        println("+----------------------+----------------------+----------------------+----------------------+")
        
        for (result in testResults) {
            println("| %-20s | %,20.2f | %,20.2f | %,20.2f |".format(
                result.testType, result.inlineTimeMs, result.normalTimeMs, result.improvement))
        }
        
        println("+----------------------+----------------------+----------------------+----------------------+")
    }
    
    // simple inline and normal function definition
    @Suppress("NOTHING_TO_INLINE")
    inline fun inlineAdd(a: Int, b: Int): Int = a + b
    fun normalAdd(a: Int, b: Int): Int = a + b
    
    // nested inline function definition
    @Suppress("NOTHING_TO_INLINE")
    inline fun inlineNestedOperation(a: Int, b: Int, c: Int): Int {
        return inlineAdd(inlineAdd(a, b), c)
    }
    
    fun normalNestedOperation(a: Int, b: Int, c: Int): Int {
        return normalAdd(normalAdd(a, b), c)
    }
    
    // complex condition inline function definition
    @Suppress("NOTHING_TO_INLINE")
    inline fun inlineComplexCondition(value: Int): Int {
        if (value % 2 == 0) {
            if (value % 3 == 0) {
                return value * 2
            } else if (value % 5 == 0) {
                return value * 3
            } else {
                return value + 1
            }
        } else {
            if (value % 3 == 0) {
                return value / 3
            } else if (value % 5 == 0) {
                return value / 5
            } else {
                return value - 1
            }
        }
    }
    
    fun normalComplexCondition(value: Int): Int {
        if (value % 2 == 0) {
            if (value % 3 == 0) {
                return value * 2
            } else if (value % 5 == 0) {
                return value * 3
            } else {
                return value + 1
            }
        } else {
            if (value % 3 == 0) {
                return value / 3
            } else if (value % 5 == 0) {
                return value / 5
            } else {
                return value - 1
            }
        }
    }
    
    // generic inline function definition
    inline fun <T> inlineGenericOperation(a: T, b: T, operation: (T, T) -> T): T = operation(a, b)
    fun <T> normalGenericOperation(a: T, b: T, operation: (T, T) -> T): T = operation(a, b)
}

fun main() {
    val benchmark = InlineBenchmark()
    
    println("Kotlin Inline Function Benchmark:")
    println("==================================")
    
    benchmark.runSimpleArithmeticTest()
    benchmark.runHigherOrderFunctionTest()
    benchmark.runLoopCallTest()
    benchmark.runNestedInlineTest()
    benchmark.runComplexConditionTest()
    benchmark.runGenericFunctionTest()
    
    benchmark.printResults()
    
    println("==================================")
    println("Kotlin Inline Function Benchmark Completed")
}
