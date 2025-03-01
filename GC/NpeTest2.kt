fun getString(): String? = if (Math.random() > 0.5) "value" else null

fun main() {
    val startTime = System.currentTimeMillis()
    val duration = 60_000
    var npeCount = 0

    while (System.currentTimeMillis() - startTime < duration) {
        val s = getString()
        val length = s?.length ?: 0 // safe call & Elvis operator
    }
    println("Kotlin (safe call) NPE count: $npeCount")
}