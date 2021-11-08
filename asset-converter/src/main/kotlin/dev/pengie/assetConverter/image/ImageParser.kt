package dev.pengie.assetConverter.image

import dev.pengie.assetConverter.Bytes
import dev.pengie.assetConverter.Bytes.toBytes
import dev.pengie.assetConverter.Path
import dev.pengie.assetConverter.ZLib
import dev.pengie.assetConverter.argsMap

object ImageParser {

    fun parsePNG(bytes: ByteArray, path: Path): ByteArray {
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
            val dataSize = Bytes.toInt(bytes[i - 4], bytes[i - 3], bytes[i - 2], bytes[i - 1])
            i+=4
            palette = bytes.copyOfRange(i, i + dataSize)
        }

        while(!(bytes[i].toInt().toChar() == 'I' &&
                bytes[i+1].toInt().toChar() == 'D' &&
                bytes[i+2].toInt().toChar() == 'A' &&
                bytes[i+3].toInt().toChar() == 'T')
        ) i++
        val dataSize = Bytes.toInt(bytes[i - 4], bytes[i - 3], bytes[i - 2], bytes[i - 1])
        i+=4

        val data = bytes.copyOfRange(i, i + dataSize)
        val decompressedData =
            ZLib.decompress(data, height + height * width * if (!indexed) colorType.components else 1)
        val rawData: ByteArray = if(!indexed) decompressedData else ByteArray(height + height * width * 3)
        if(indexed) {
            var offset = 0
            decompressedData.forEachIndexed { index, byte ->
                if((index % (width + 1) != 0)) {
                    val j = (index - offset) * 3 + offset
                    //println("$j off: $offset i: $index")
                    // TODO("Fix weird diagonal offset")

                    val k = byte.toUByte().toInt() * 3
                    rawData[j] = palette!![k]
                    rawData[j + 1] = palette[k + 1]
                    rawData[j + 2] = palette[k + 2]
                } else
                    offset++
            }
        }
        val unfilteredData = rawData.filterIndexed { index, _ -> index % (width * colorType.components + 1) != 0}.toByteArray()
        return constructImageAsset(Image(width, height, unfilteredData, colorType), path)
    }

    /**
     * Texture Type - 0
     * 4 bytes - width
     * 4 bytes - height
     * 1 bytes - color format
     * 1 byte - interpolation type (0 - Linear, 1 - Nearest)
     * n data - compressed (deflated) data
     */
    private fun constructImageAsset(image: Image, path: Path): ByteArray {
        val interpolation = when(argsMap["interpolation"]) {
            null -> 0
            "linear" -> 0
            "nearest" -> 1
            else -> 0
        }

        return byteArrayOf(
            *Bytes.generateHeader(0, path),
            *image.width.toBytes(),
            *image.height.toBytes(),
            image.colorFormat.components.toByte(),
            interpolation.toByte(),
            *ZLib.compress(image.data)
        )
    }
}