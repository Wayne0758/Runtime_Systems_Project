# Comparative Study of Runtime Systems for Java and Kotlin in Android Development
## Project Background
Although Java and Kotlin share the JVM runtime environment, their language design differences (such as null safety and coroutines) lead to significant divergences in memory management, compilation optimization, and runtime behavior. This project analyzes the performance characteristics of the two languages on ART/JVM from the perspective of the runtime system, providing Android developers with optimization decision-making insights based on runtime features.

## Project Objectives
- **Execution Performance**: Context switching overhead of coroutines vs. thread pools (CPU cycles/switch)
- **Memory Efficiency**: Memory allocation rate of Lambda expressions/higher-order functions (MB/s)
- **GC Behavior**: Impact of null safety mechanisms on the frequency of NullPointerException (occurrences/minute)
- **Compilation Optimization**: Strategy differences between Kotlin inline functions and JIT auto-inlining (percentage of inlined methods)


