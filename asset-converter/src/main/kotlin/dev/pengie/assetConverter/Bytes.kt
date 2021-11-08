package dev.pengie.assetConverter

import java.nio.file.Files
import java.nio.file.Paths

object Bytes {

    private val headerData = byteArrayOf(*("KTASSET".encodeToByteArray()), 0x0)
    fun generateHeader(type: Int, path: Path): ByteArray {
        val modifiedTime = Files.getLastModifiedTime(Paths.get(path.fullPath)).toMillis()
        return byteArrayOf(*headerData, type.toUInt().toByte(), *modifiedTime.toBytes())
    }

    /**
     * Converts the four bytes to a big endian integer.
     */
    fun toInt(ba: Byte, bb: Byte, bc: Byte, bd: Byte): Int =
        ((ba.toUByte().toInt() shl 24) or (bb.toUByte().toInt() shl 16) or (bc.toUByte().toInt() shl 8) or bd.toUByte().toInt())

    fun toLong(ba: Byte, bb: Byte, bc: Byte, bd: Byte, be: Byte, bf: Byte, bg: Byte, bh: Byte): Long =
        ((ba.toUByte().toLong() shl 56) or (bb.toUByte().toLong() shl 48) or (bc.toUByte().toLong() shl 40) or (bd.toUByte().toLong() shl 32) or (be.toUByte().toLong() shl 24) or (bf.toUByte().toLong() shl 16) or (bg.toUByte().toLong() shl 8) or bh.toUByte().toLong())


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

    fun Long.toBytes(): ByteArray =
        byteArrayOf((this shr 56).toByte(), (this shr 48).toByte(), (this shr 40).toByte(), (this shr 32).toByte(), (this shr 24).toByte(), (this shr 16).toByte(), (this shr 8).toByte(), this.toByte())
}