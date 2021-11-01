package data

import dev.pengie.kotaro.assets.Asset
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.utils.Bytes.toBytes
import dev.pengie.kotaro.utils.Bytes.toInt

fun ByteArray.toChunk(name: String) = byteArrayOf(
    *name.encodeToByteArray(),
    *this.size.toBytes(),
    *this
)

fun Asset<Any>.serializeData(): ByteArray = byteArrayOf(
    *(this.assetName.encodeToByteArray()), 0x0,
    *(this.path.fullPath.encodeToByteArray()), 0x0,
    *(this.type.simpleName!!.encodeToByteArray()), 0x0
)

fun Color.serializeData(): ByteArray = byteArrayOf(
    this.r.toByte(),
    this.g.toByte(),
    this.b.toByte(),
    this.a.toByte()
)

fun ByteArray.checkString(index: Int, string: String): Boolean {
    string.forEachIndexed {i, c -> if(index + i == this.size || this[index + i].toInt().toChar() != c) return false }
    return true
}

fun toFloat(ba: Byte, bb: Byte, bc: Byte, bd: Byte): Float =
    java.lang.Float.intBitsToFloat(toInt(ba, bb, bc, bd))

fun ByteArray.readInt(index: Int): Int = toInt(this[index], this[index + 1], this[index + 2], this[index + 3])
fun ByteArray.readFloat(index: Int): Float = toFloat(this[index], this[index + 1], this[index + 2], this[index + 3])
fun ByteArray.readColor(index: Int): Color = Color(this[index].toUByte().toInt(), this[index + 1].toUByte().toInt(), this[index + 2].toUByte().toInt(), this[index + 3].toUByte().toInt())

fun ByteArray.readString(start: Int): String {
    var i = 0
    val builder = StringBuilder()
    while(start + i < this.size && this[start + i].toInt() != 0x00) {
        builder.append(this[start + i].toInt().toChar())
        i++
    }
    return builder.toString()
}

fun ByteArray.readString(start: Int, length: Int): String =
    ((start) until start + length).map { this[it].toInt().toChar() }.toCharArray().concatToString()

fun ByteArray.advanceUntil(start: Int, value: String): Int {
    var index = start
    while(index + value.length < this.size)
        if(checkString(index, value))
            break
        else
            index++
    if(index + value.length == this.size)
        return -1
    index += value.length
    return index
}