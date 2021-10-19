package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.scene.components.MeshRenderer
import kotlin.reflect.KClass

abstract class BatchRenderer<T: Model> {
    private val defaultMeshRenderer = MeshRenderer()

    protected val batch: MutableList<RenderData<T>> = mutableListOf()

    abstract fun bindWindowLayer()
    abstract fun viewport(width: Int, height: Int)
    abstract fun clearScreen()
    abstract fun clearColor(color: Color)

    fun begin() {
        batch.clear()
    }

    fun render(model: T, meshRenderer: MeshRenderer?) {
        batch.add(RenderData(model, meshRenderer ?: defaultMeshRenderer))
    }
    fun end() = renderBatch()

    protected abstract fun renderBatch()

    protected data class RenderData<T: Model> (val model: T, val meshData: MeshRenderer)
}

expect object RendererFactory {
    operator fun invoke(): BatchRenderer<Model>
}