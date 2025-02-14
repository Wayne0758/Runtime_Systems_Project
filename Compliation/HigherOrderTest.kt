inline fun inlineOperation(a: Int, b: Int, op: (Int, Int) -> Int): Int {
    return op(a, b)
}

fun normalOperation(a: Int, b: Int, op: (Int, Int) -> Int): Int {
    return op(a, b)
}
