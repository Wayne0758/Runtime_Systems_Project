fun main() {
    val start = System.currentTimeMillis()
    while (System.currentTimeMillis() - start < 60_000) {
        for (i in 0 until 10_000) {
            val s = "Object-$i" // build a short-lived object
        }
    }
}