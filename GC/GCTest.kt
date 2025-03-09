import java.lang.management.ManagementFactory

fun main() {   
    // Get GC MXBeans
    val gcBeans = ManagementFactory.getGarbageCollectorMXBeans()
    var totalGcCountBefore = 0L
    var totalGcTimeBefore = 0L
    
    // Record GC statistics at the beginning
    for (gcBean in gcBeans) {
        totalGcCountBefore += gcBean.collectionCount
        totalGcTimeBefore += gcBean.collectionTime
    }
    
    val start = System.currentTimeMillis()
    var objectsCreated = 0L
    val testDuration = 60_000L // One minute
    val progressBarWidth = 50
    
    // Run for one minute, creating many short-lived objects
    while (System.currentTimeMillis() - start < testDuration) {
        // Calculate and display progress
        val elapsed = System.currentTimeMillis() - start
        val progress = ((elapsed * progressBarWidth) / testDuration).toInt()
        val percent = ((elapsed * 100) / testDuration).toInt()
        
        // Create progress bar string
        val progressBar = StringBuilder("\r[")
        for (i in 0 until progressBarWidth) {
            when {
                i < progress -> progressBar.append("=")
                i == progress -> progressBar.append(">")
                else -> progressBar.append(" ")
            }
        }
        progressBar.append("] ").append(percent).append("% ")
        
        // Display progress
        print(progressBar.toString())
        
        // Create objects
        for (i in 0 until 100_000) {
            val objects = Array<Any?>(100) { null } // Create array objects
            for (j in 0 until 100) {
                objects[j] = "Object-$i-$j" // Create string objects
            }
            objectsCreated += 100
        }
    }
    
    // Clear progress bar line and move to next line
    print("\r" + " ".repeat(progressBarWidth + 10) + "\r")
    
    // Record GC statistics at the end
    var totalGcCountAfter = 0L
    var totalGcTimeAfter = 0L
    for (gcBean in gcBeans) {
        totalGcCountAfter += gcBean.collectionCount
        totalGcTimeAfter += gcBean.collectionTime
    }
    
    // Calculate GC count and time
    val gcCount = totalGcCountAfter - totalGcCountBefore
    val gcTimeMs = totalGcTimeAfter - totalGcTimeBefore
    
    // Output the results in a table format
    println("+-------------------------+------------------------+")
    println("| Metric                  | Value                  |")
    println("+-------------------------+------------------------+")
    println("| Objects Created         | ${String.format("%,22d", objectsCreated)} |")
    println("| GC Count (per minute)   | ${String.format("%,22d", gcCount)} |")
    println("| GC Time (ms per minute) | ${String.format("%,22d", gcTimeMs)} |")
    println("+-------------------------+------------------------+")
}