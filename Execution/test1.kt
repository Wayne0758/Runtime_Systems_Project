import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class ThreadBenchmark {
    private val counter = AtomicLong(0)
    
    fun runBenchmark(numTasks: Int, numIterations: Int) {
        val executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        )
        
        val startTime = System.currentTimeMillis()
        val latch = CountDownLatch(numTasks)
        
        repeat(numTasks) {
            executor.submit {
                try {
                    repeat(numIterations) {
                        Thread.yield() // Force context switch
                        counter.incrementAndGet()
                    }
                } finally {
                    latch.countDown()
                }
            }
        }
        
        latch.await()
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime
        
        val totalSwitches = counter.get()
        val switchesPerSecond = totalSwitches * 1000.0 / totalTime
        
        println("""
            Thread Pool Benchmark Results:
            Total time: $totalTime ms
            Total context switches: $totalSwitches
            Switches per second: $switchesPerSecond
            CPU time per switch: ${totalTime.toDouble() / totalSwitches} ms
        """.trimIndent())
        
        executor.shutdown()
    }
}

fun main() {
    println("Starting Thread Pool Benchmark...")
    val numTasks = 1000
    val numIterations = 10000
    ThreadBenchmark().runBenchmark(numTasks, numIterations)
}