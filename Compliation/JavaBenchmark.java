import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
public class JavaBenchmark {
    private static final int TEST_DATA = 1000;

    @Benchmark
    public int testJavaMethod() {
        return javaAdd(TEST_DATA, TEST_DATA);
    }

    public static int javaAdd(int a, int b) {
        return a + b;
    }
}
