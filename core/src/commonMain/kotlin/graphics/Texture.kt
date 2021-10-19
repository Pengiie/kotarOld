package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.types.Disposable

interface Texture : Disposable {
    fun init(data: TexData)
    fun bind()
    fun unbind()
}

class TexData(val data: Array<Byte> = emptyArray(), val width: Int, val height: Int)

expect object TextureFactory {
    operator fun invoke(): Texture
}