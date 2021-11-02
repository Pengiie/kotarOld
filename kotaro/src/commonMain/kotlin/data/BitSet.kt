package dev.pengie.kotaro.data

expect class BitSet(length: Int) {
    fun set(pos: Int)
    fun reset(pos: Int)

    operator fun get(pos: Int): Boolean
}