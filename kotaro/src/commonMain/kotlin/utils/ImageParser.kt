package dev.pengie.kotaro.utils

import dev.pengie.kotaro.data.ColorFormat
import dev.pengie.kotaro.data.Interpolation
import dev.pengie.kotaro.graphics.TexData
import dev.pengie.kotaro.utils.Bytes.checkString

object ImageParser {

    fun parse(bytes: ByteArray): TexData {
        if(!bytes.checkString(0, "KTASSET"))
            throw Exception("Incorrect header data in asset file")
        if(bytes[0x8].toInt() != 0)
            throw Exception("Wrong asset typing for texture")
        var i = 0x11
        val width = Bytes.toInt(bytes[i], bytes[++i], bytes[++i], bytes[++i])
        val height = Bytes.toInt(bytes[++i], bytes[++i], bytes[++i], bytes[++i])
        val colorFormat: ColorFormat = when(bytes[++i].toInt()) {
            1 -> ColorFormat.G
            2 -> ColorFormat.GA
            3 -> ColorFormat.RGB
            4 -> ColorFormat.RGBA
            else -> throw Exception("Unknown color format")
        }
        val interpolation: Interpolation = when(bytes[++i].toInt()) {
            0 -> Interpolation.LINEAR
            1 -> Interpolation.NEAREST
            else -> throw Exception("Unknown interpolation value")
        }
        val data = ZLib.decompress(bytes.copyOfRange(++i, bytes.size), width * height * colorFormat.components)
        return TexData(data, width, height, colorFormat, interpolation)
    }
}