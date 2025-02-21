import java.lang.management.ManagementFactory

object KotlinLambdaMemoryBenchmark {
    const val ITERATIONS = 1_000_000

    @JvmStatic
    fun main(args: Array<String>) {
        println("Running Kotlin Lambda Function Benchmark...")
        forceGC()
        measureAllocationRate("Kotlin Lambda Function", ::benchmarkLambdaFunction)

        println("\nRunning Kotlin Higher-Order Function Benchmark...")
        forceGC()
        measureAllocationRate("Kotlin Higher-Order Function", ::benchmarkHigherOrderFunction)
    }

    fun forceGC() {
        System.gc()
        Thread.sleep(500)
    }

    fun measureAllocationRate(testName: String, benchmark: () -> Unit) {
        val startTime = System.nanoTime()
        val startMemory = getUsedMemory()

        benchmark()

        val endMemory = getUsedMemory()
        val endTime = System.nanoTime()

        val elapsedTimeSeconds = (endTime - startTime) / 1_000_000_000.0
        val allocatedMemoryMB = (endMemory - startMemory) / (1024.0 * 1024.0)
        val allocationRate = allocatedMemoryMB / elapsedTimeSeconds

        println("$testName Allocation Rate: %.2f MB/s".format(allocationRate))
    }

    fun benchmarkLambdaFunction() {
        val operations = Array<(Int, Int) -> Int>(ITERATIONS) { { a, b -> a + b } }
    }

    fun benchmarkHigherOrderFunction() {
        val operations = Array<(Int, Int) -> Int>(ITERATIONS) { ::higherOrderFunction }
    }


    fun higherOrderFunction(a: Int, b: Int): Int {
        return a + b
    }


    fun getUsedMemory(): Long {
        val memoryBean = ManagementFactory.getMemoryMXBean()
        return memoryBean.heapMemoryUsage.used
    }
}
