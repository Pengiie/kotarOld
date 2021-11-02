package dev.pengie.kotaro.utils

import dev.pengie.kotaro.data.ColorFormat
import dev.pengie.kotaro.graphics.TexData

object ImageParser {

    fun parsePNG(bytes: ByteArray): TexData {
        val width = Bytes.toInt(bytes[0x10], bytes[0x11], bytes[0x12], bytes[0x13])
        val height = Bytes.toInt(bytes[0x14], bytes[0x15], bytes[0x16], bytes[0x17])
        //val bitDepth = bytes[0x18].toInt()
        val colorType: ColorFormat = when (bytes[0x19].toInt()) {
            0 -> ColorFormat.G
            2 -> ColorFormat.RGB
            3 -> ColorFormat.RGB
            4 -> ColorFormat.GA
            6 -> ColorFormat.RGBA
            else -> throw Exception("PNG Color Format Not Supported!!")
        }
        val indexed = bytes[0x19].toInt() == 3
        //val interlace = bytes[0x1D].toInt()

        var i = 0x1D
        var palette: ByteArray? = null
        if(indexed) {
            while(!(bytes[i].toInt().toChar() == 'P' &&
                        bytes[i+1].toInt().toChar() == 'L' &&
                        bytes[i+2].toInt().toChar() == 'T' &&
                        bytes[i+3].toInt().toChar() == 'E')
            ) i++
            val dataSize = Bytes.toInt(bytes[i-4], bytes[i-3], bytes[i-2], bytes[i-1])
            i+=4
            palette = bytes.copyOfRange(i, i + dataSize)
        }

        while(!(bytes[i].toInt().toChar() == 'I' &&
                bytes[i+1].toInt().toChar() == 'D' &&
                bytes[i+2].toInt().toChar() == 'A' &&
                bytes[i+3].toInt().toChar() == 'T')
        ) i++
        val dataSize = Bytes.toInt(bytes[i-4], bytes[i-3], bytes[i-2], bytes[i-1])
        i+=4

        val data = bytes.copyOfRange(i, i + dataSize)
        val decompressedData = ZLib.decompress(data, height + height * width * if(!indexed) colorType.components else 1)
        val rawData: ByteArray = if(!indexed) decompressedData else ByteArray(decompressedData.size * 3)
        if(indexed) {
            decompressedData.forEachIndexed { index, byte ->
                val j = index * 3
                val k = byte.toUByte().toInt() * 3
                rawData[j] = palette!![k]
                rawData[j+1] = palette[k+1]
                rawData[j+2] = palette[k+2]
            }
        }
        val unfilteredData = rawData.filterIndexed { index, _ -> index % (width * colorType.components + 1) != 0}.toByteArray()
        return TexData(unfilteredData, width, height, colorType)
    }
}