inline fun recursiveInline(n: Int): Int {
    return if (n <= 0) 0 else recursiveInline(n - 1) + 1
}

fun normalRecursive(n: Int): Int {
    return if (n <= 0) 0 else normalRecursive(n - 1) + 1
}
