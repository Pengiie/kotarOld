package dev.pengie.kotaro.utils

actual object FloatBytes {
    actual fun toFloat(ba: Byte, bb: Byte, bc: Byte, bd: Byte): Float =
        java.lang.Float.intBitsToFloat(Bytes.toInt(ba, bb, bc, bd))
}