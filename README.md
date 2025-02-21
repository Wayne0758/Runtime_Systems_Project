# Comparative Study of Runtime Systems for Java and Kotlin in Android Development
## Project Background
Although Java and Kotlin share the JVM runtime environment, their language design differences (such as null safety and coroutines) lead to significant divergences in memory management, compilation optimization, and runtime behavior. This project analyzes the performance characteristics of the two languages on ART/JVM from the perspective of the runtime system, providing Android developers with optimization decision-making insights based on runtime features.

## Project Objectives
- **Execution Performance**: Context switching overhead of coroutines vs. thread pools (CPU cycles/switch)

| | Java | Kotlin | 
| --- | --- | --- |
| Total time | 644 ms | 686 ms |
| Total context switches | 10000000 | 10000000 |
| Switches per second | 15527950.31 | 14577259.48 |
| CPU time per switch | 0.000064 ms | 0.000069 ms |
- **Memory Efficiency**: Memory allocation rate of Lambda expressions/higher-order functions (MB/s)

| | Java | Kotlin | 
| --- | --- | --- |
| Lambda Allocation | 6.0 MB | 4.0 MB |
| Lambda Allocation Execution Time | 38.8739 ms | 26.6284 ms |
| Higher-Order Function Allocation | 6.0 MB | 4.0 MB |
| Higher-Order Function Allocation Execution Time | 39.5942 ms | 27.7174 ms |
- **GC Behavior**: Impact of null safety mechanisms on the frequency of NullPointerException (occurrences/minute)
- **Compilation Optimization**: Strategy differences between Kotlin inline functions and JIT auto-inlining (percentage of inlined methods)

| Test Type          | Kotlin Inline | JIT Auto-Inlining |
|--------------------|---------------|-------------------|
| Simple Arithmetic  | 100%          | 92%              |
| Higher-Order Functions | 98%       | 45%              |
| Recursive Calls    | Not Supported | 78% (Depth ≤ 15) |
| Calls within Loops | 100%          | 89%              |

