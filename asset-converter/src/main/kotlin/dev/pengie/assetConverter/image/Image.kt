package dev.pengie.assetConverter.image

class Image(val width: Int, val height: Int, val data: ByteArray, val colorFormat: ColorFormat)

enum class ColorFormat(val components: Int) {
    G(1),
    GA(2),
    RGB(3),
    RGBA(4);
}