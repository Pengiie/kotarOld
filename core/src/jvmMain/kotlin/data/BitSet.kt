package dev.pengie.kotaro.data

actual class BitSet actual constructor(length: Int) {
    val set = java.util.BitSet(length)

    actual fun set(pos: Int) {
        set.set(pos)
    }
    actual fun reset(pos: Int) {
        set.clear(pos)
    }

    actual operator fun get(pos: Int): Boolean = set[pos]
}