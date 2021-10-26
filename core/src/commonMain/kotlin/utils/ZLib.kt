package dev.pengie.kotaro.utils

expect object ZLib {
    fun compress(arr: ByteArray): ByteArray
    fun decompress(arr: ByteArray, expectedSize: Int): ByteArray
}