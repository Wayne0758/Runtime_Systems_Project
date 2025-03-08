import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class JavaHigherOrderBenchmark {
    private static final int ITERATIONS = 1_000_000;

    @FunctionalInterface
    public interface IntOperation {
        int apply(int a, int b);
    }

    public static void main(String[] args) {
        benchmarkHigherOrderFunction();
    }

    // Higher-Order Function Benchmark
    public static void benchmarkHigherOrderFunction() {
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

        IntOperation[] operations = new IntOperation[ITERATIONS];
        for (int i = 0; i < ITERATIONS; i++) {
            operations[i] = JavaHigherOrderBenchmark::higherOrderFunction;  // Using method reference
        }

        long endMemory = getUsedMemory();
        long endTime = System.nanoTime();

        double elapsedTimeMs = (endTime - startTime) / 1_000_000.0;
        double allocatedMemoryMB = (endMemory - startMemory) / (1024.0 * 1024.0);

        System.out.println("Java Higher-Order Function Allocation: " + allocatedMemoryMB + " MB");
        System.out.println("Execution Time: " + elapsedTimeMs + " ms");
    }

    // Regular Higher-Order Function
    public static int higherOrderFunction(int a, int b) {
        return a + b;
    }

    // Memory Usage Tracking
    private static long getUsedMemory() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed();
    }
}
