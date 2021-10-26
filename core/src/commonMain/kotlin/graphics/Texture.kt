package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.data.ColorFormat
import dev.pengie.kotaro.data.Interpolation
import dev.pengie.kotaro.types.Disposable

interface Texture : Disposable {
    fun init(data: TexData)
    fun bind()
    fun unbind()
}

class TexData(
    val data: ByteArray = byteArrayOf(),
    val width: Int,
    val height: Int,
    val format: ColorFormat = ColorFormat.RGBA,
    var interpolation: Interpolation = Interpolation.LINEAR
    )

expect object TextureFactory {
    operator fun invoke(): Texture
}