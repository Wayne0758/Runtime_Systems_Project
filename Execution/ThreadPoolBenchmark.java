import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolBenchmark {
    private final AtomicLong counter = new AtomicLong(0);
    
    public void runThreadPoolBenchmark(int numTasks, int numIterations) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        );
        
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(numTasks);
        
        for (int i = 0; i < numTasks; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < numIterations; j++) {
                        Thread.yield(); // Force context switch
                        counter.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        long totalSwitches = counter.get();
        double switchesPerSecond = totalSwitches * 1000.0 / totalTime;
        
        System.out.printf("""
            Thread Pool Benchmark Results:
            Total time: %d ms
            Total context switches: %d
            Switches per second: %.2f
            CPU time per switch: %.6f ms
            """, 
            totalTime, totalSwitches, switchesPerSecond,
            (double) totalTime / totalSwitches
        );
        
        executor.shutdown();
    }

    // Main method to run the benchmark
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Thread Pool Benchmark...");
        int numTasks = 1000;
        int numIterations = 10000;
        new ThreadPoolBenchmark().runThreadPoolBenchmark(numTasks, numIterations);
    }
}