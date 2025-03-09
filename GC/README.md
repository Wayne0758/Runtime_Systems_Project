# GC behavior and NPE frequency testing

## Testing Content

### GC Behavior Test
- `GCTest.java`: Java version of GC test, creating a large number of short-lived objects and monitoring GC activity
- `GCTest.kt`: Kotlin version of GC test, with the same functionality as the Java version

### NPE Frequency Test
- `NpeTest.java`: Java version of NPE test, without null checks
- `NpeTestForced.kt`: Kotlin version of NPE test, using the `!!` operator for forced dereferencing
- `NpeTestSafe.kt`: Kotlin version of NPE test, using null safety mechanisms (`?.` and `?:`)

## Running the Tests

### Prerequisites
- JDK 8 or higher
- Kotlin compiler (kotlinc)

### Running with Script
1. Grant execution permission to the script:
   ```
   chmod +x run_gc_tests.sh
   ```

2. Run the script:
   ```
   ./run_gc_tests.sh
   ```

## Output Format

All test results are output in table format for easy reading and comparison. For example:

```
Java GC Test Results:
+-------------------------+------------------------+
| Metric                  | Value                  |
+-------------------------+------------------------+
| Objects Created         | 3,730,000,000          |
| GC Count (per minute)   | 1,430                  |
| GC Time (ms per minute) | 433                    |
+-------------------------+------------------------+
```

## Test Results

### GC Behavior Test Results

| Metric | Java | Kotlin |
| --- | --- | --- |
| Objects Created (1 minute) | 3,730,000,000 | 3,810,000,000 |
| GC Count (per minute) | 1,430 | 976 |
| GC Time (ms per minute) | 433 | 447 |

### NPE Frequency Test Results

| Test Scenario | Total Count (per minute) | NPE Count (per minute)| NPE Percentage |
| --- | --- | --- | --- |
| Java (No Null Check) | 1,946,359,943 | 973,129,579 | 49.997% |
| Kotlin (Using !!) | 143,851,077 | 71,930,730 | 50.004% |
| Kotlin (Null Safety) | 1,987,267,338 | 0 | 0% |

## Conclusions

1. **GC Behavior**:
   - Kotlin creates more objects in the same time period (about 2.1% more)
   - Java has more GC cycles than Kotlin (about 46.5% more), suggesting that Kotlin's object allocation and collection may be more efficient
   - The total GC time for both languages is similar, indicating that the JVM handles garbage collection with similar efficiency for both languages

2. **NPE Frequency**:
   - The NPE percentage for Java and Kotlin(!!) is almost identical (about 50%)
   - Kotlin's null safety mechanism completely eliminates NPEs
   - Java processes NPEs about 13.5 times faster than Kotlin(!!), possibly because Kotlin's null safety checking mechanism adds runtime overhead 