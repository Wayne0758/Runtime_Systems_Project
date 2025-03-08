import java.lang.management.ManagementFactory

object KotlinHigherOrderBenchmark {
    const val ITERATIONS = 1_000_000  // ✅ Public constant for inline compatibility

    @JvmStatic
    fun main(args: Array<String>) {
        benchmarkHigherOrderFunction()
    }

    /** ✅ HIGHER-ORDER FUNCTION BENCHMARK */
    fun benchmarkHigherOrderFunction() {
        val startTime = System.nanoTime()
        val startMemory = getUsedMemory()

        val operations = Array<(Int, Int) -> Int>(ITERATIONS) { { a, b -> higherOrderFunction(a, b) } }

        val endMemory = getUsedMemory()
        val endTime = System.nanoTime()

        val elapsedTimeMs = (endTime - startTime) / 1_000_000.0
        val allocatedMemoryMB = (endMemory - startMemory) / (1024.0 * 1024.0)

        println("Kotlin Higher-Order Function Allocation: $allocatedMemoryMB MB")
        println("Execution Time: $elapsedTimeMs ms")
    }

    /** ✅ Regular Higher-Order Function */
    fun higherOrderFunction(a: Int, b: Int): Int {
        return a + b
    }

    /** ✅ Memory Usage Tracking */
    fun getUsedMemory(): Long {
        val memoryBean = ManagementFactory.getMemoryMXBean()
        return memoryBean.heapMemoryUsage.used
    }
}
