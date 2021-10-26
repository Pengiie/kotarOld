package dev.pengie.kotaro.utils

object Bytes {

    /**
     * Converts the four bytes to a big endian integer.
     */
    fun toInt(ba: Byte, bb: Byte, bc: Byte, bd: Byte): Int =
        ((ba.toUByte().toInt() shl 24) or (bb.toUByte().toInt() shl 16) or (bc.toUByte().toInt() shl 8) or bd.toUByte().toInt())

}