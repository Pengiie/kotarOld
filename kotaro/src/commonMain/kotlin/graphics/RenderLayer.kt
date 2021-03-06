package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.types.Disposable

abstract class RenderLayer(val width: Int, val height: Int, val type: RenderLayerType = RenderLayerType.DEFAULT) : Disposable {
    abstract fun init()
    abstract fun bind()
    abstract fun unbind()

    abstract fun getTexture(): Texture
}

enum class RenderLayerType {
    DEFAULT,
    DEPTH
}

expect object RenderLayerFactory {
    operator fun invoke(width: Int, height: Int, type: RenderLayerType = RenderLayerType.DEFAULT): RenderLayer
}