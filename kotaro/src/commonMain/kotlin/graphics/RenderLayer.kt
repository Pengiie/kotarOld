package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.types.Disposable

abstract class RenderLayer(val width: Int, val height: Int) : Disposable {
    abstract fun init()
    abstract fun bind()
    abstract fun unbind()

    abstract fun getTexture(): Texture
}

expect object RenderLayerFactory {
    operator fun invoke(width: Int, height: Int): RenderLayer
}