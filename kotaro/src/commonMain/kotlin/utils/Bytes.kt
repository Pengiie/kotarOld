package dev.pengie.kotaro.utils

object Bytes {

    /**
     * Converts the four bytes to a big endian integer.
     */
    fun toInt(ba: Byte, bb: Byte, bc: Byte, bd: Byte): Int =
        ((ba.toUByte().toInt() shl 24) or (bb.toUByte().toInt() shl 16) or (bc.toUByte().toInt() shl 8) or bd.toUByte().toInt())

    /**
     * Converts the four bytes to a little endian integer.
     */
    fun toLittleInt(ba: Byte, bb: Byte, bc: Byte, bd: Byte): Int =
        toInt(bd, bc, bb, ba)

    fun ByteArray.checkString(index: Int, string: String): Boolean {
        string.forEachIndexed {i, c -> if(index + i == this.size || this[index + i].toInt().toChar() != c) return false }
        return true
    }

    fun Float.toBytes(): ByteArray =
        this.toRawBits().toBytes()

    fun Int.toBytes(): ByteArray =
        byteArrayOf((this shr 24).toByte(), (this shr 16).toByte(), (this shr 8).toByte(), this.toByte())
}

expect object FloatBytes {
    fun toFloat(ba: Byte, bb: Byte, bc: Byte, bd: Byte): Float
}