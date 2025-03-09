fun getStringForSafeTest(): String? = if (Math.random() > 0.5) "value" else null

fun main() {
    val startTime = System.currentTimeMillis()
    val duration = 60_000L // One minute
    var npeCount = 0L
    var totalCalls = 0L
    val progressBarWidth = 50

    while (System.currentTimeMillis() - startTime < duration) {
        // Calculate and display progress
        val elapsed = System.currentTimeMillis() - startTime
        val progress = ((elapsed * progressBarWidth) / duration).toInt()
        val percent = ((elapsed * 100) / duration).toInt()
        
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
        
        // Run NPE test with safe calls
        for (i in 0 until 1000) {
            val s = getStringForSafeTest()
            val length = s?.length ?: 0 // Safe call and Elvis operator
            totalCalls++
        }
    }
    
    // Clear progress bar line and move to next line
    print("\r" + " ".repeat(progressBarWidth + 10) + "\r")
    
    // Output the results in a table format
    println("+-------------------------+------------------------+")
    println("| Metric                  | Value                  |")
    println("+-------------------------+------------------------+")
    println("| Total Calls             | ${String.format("%,22d", totalCalls)} |")
    println("| NPE Count               | ${String.format("%,22d", npeCount)} |") // Always 0
    println("| NPE per Minute          | ${String.format("%,22d", 0)} |")
    println("| NPE Percentage          | ${String.format("%,21.3f%%", 0.0)} |")
    println("+-------------------------+------------------------+")
} 