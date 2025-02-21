import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class JavaLambdaMemoryBenchmark {
    private static final int ITERATIONS = 1_000_000;

    @FunctionalInterface
    public interface IntOperation {
        int apply(int a, int b);
    }

    public static void main(String[] args) {
        System.out.println("Running Java Lambda Function Benchmark...");
        forceGC();  // Ensure clean memory before test
        measureAllocationRate("Java Lambda Function", JavaLambdaMemoryBenchmark::benchmarkLambdaFunction);

        System.out.println("\nRunning Java Higher-Order Function Benchmark...");
        forceGC();
        measureAllocationRate("Java Higher-Order Function", JavaLambdaMemoryBenchmark::benchmarkHigherOrderFunction);
    }

    public static void forceGC() {
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void measureAllocationRate(String testName, Runnable benchmark) {
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

        benchmark.run();

        long endMemory = getUsedMemory();
        long endTime = System.nanoTime();

        double elapsedTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
        double allocatedMemoryMB = (endMemory - startMemory) / (1024.0 * 1024.0);
        double allocationRate = allocatedMemoryMB / elapsedTimeSeconds;

        System.out.printf("%s Allocation Rate: %.2f MB/s%n", testName, allocationRate);
    }

    public static void benchmarkLambdaFunction() {
        IntOperation[] operations = new IntOperation[ITERATIONS];
        for (int i = 0; i < ITERATIONS; i++) {
            operations[i] = (a, b) -> a + b;
        }
    }

    public static void benchmarkHigherOrderFunction() {
        IntOperation[] operations = new IntOperation[ITERATIONS];
        for (int i = 0; i < ITERATIONS; i++) {
            operations[i] = JavaLambdaMemoryBenchmark::higherOrderFunction;
        }
    }

    public static int higherOrderFunction(int a, int b) {
        return a + b;
    }

    private static long getUsedMemory() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed();
    }
}
