package dev.pengie.kotaro.scene.components

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowResizeEvent
import dev.pengie.kotaro.graphics.RenderLayer
import dev.pengie.kotaro.graphics.RenderLayerFactory
import dev.pengie.kotaro.math.Matrix4f
import dev.pengie.kotaro.math.toRadians
import dev.pengie.kotaro.types.Disposable

enum class ProjectionType {
    Orthographic,
    Perspective
}

class Camera (
    projection: ProjectionType = ProjectionType.Perspective,
    fov: Float = 90f,
    nearPlane: Float = 0.05f,
    farPlane: Float = 1000f,
    val backgroundColor: Color = Color(0x4D5DB8FF)
) : Disposable {
    var projection: ProjectionType = projection
        set(value) {
            field = value
            createProjectionMatrix(Application.window.width, Application.window.height)
        }
    var fov: Float = fov
        set(value) {
            field = value
            createProjectionMatrix(Application.window.width, Application.window.height)
        }
    var nearPlane: Float = nearPlane
        set(value) {
            field = value
            createProjectionMatrix(Application.window.width, Application.window.height)
        }
    var farPlane: Float = farPlane
        set(value) {
            field = value
            createProjectionMatrix(Application.window.width, Application.window.height)
        }

    var renderLayer = RenderLayerFactory(Application.window.width, Application.window.height).apply(RenderLayer::init)
        private set
    var projectionMatrix: Matrix4f = createProjectionMatrix(Application.window.width, Application.window.height)
        private set

    init {
        EventManager.registerListener(EventListener.create<WindowResizeEvent> { event ->
            renderLayer.dispose()
            renderLayer = RenderLayerFactory(event.width, event.height).apply(RenderLayer::init)
            projectionMatrix = createProjectionMatrix(event.width, event.height)
        })
    }

    private fun createProjectionMatrix(width: Int, height: Int): Matrix4f =
        if(projection == ProjectionType.Perspective)
            Matrix4f.perspective(
                fov.toRadians(),
                width.toFloat() / height.toFloat(),
                nearPlane,
                farPlane
            )
        else
            Matrix4f()

    override fun dispose() = renderLayer.dispose()
}