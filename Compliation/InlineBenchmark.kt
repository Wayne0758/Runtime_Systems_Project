import org.openjdk.jmh.annotations.*

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
open class InlineBenchmark {
    private val testData = 1000

    @Benchmark
    fun testKotlinInline(): Int {
        return inlineAdd(testData, testData)
    }

    @Benchmark
    fun testKotlinNormal(): Int {
        return normalAdd(testData, testData)
    }

    @Benchmark
    fun testInlineLambda(): Int {
        return inlineOperation(testData, testData) { a, b -> a xor b }
    }

    @Benchmark
    fun testNormalLambda(): Int {
        return normalOperation(testData, testData) { a, b -> a xor b }
    }

    inline fun inlineAdd(a: Int, b: Int): Int = a + b
    fun normalAdd(a: Int, b: Int): Int = a + b
    
    inline fun inlineOperation(a: Int, b: Int, op: (Int, Int) -> Int): Int = op(a, b)
    fun normalOperation(a: Int, b: Int, op: (Int, Int) -> Int): Int = op(a, b)
}
