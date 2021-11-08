package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowResizeEvent
import dev.pengie.kotaro.graphics.shader.Shader
import dev.pengie.kotaro.graphics.shader.ShaderFactory
import dev.pengie.kotaro.graphics.shader.Shaders
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.math.toMatrix4f
import dev.pengie.kotaro.math.toVector3f
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.scene.components.light.AmbientLight
import dev.pengie.kotaro.scene.components.light.DirectionalLight
import dev.pengie.kotaro.scene.components.light.PointLight
import dev.pengie.kotaro.types.Disposable


object RenderSystem : Disposable {

    internal val shaders: HashMap<String, Shader> = hashMapOf()
    private val depthRenderer: DepthRenderer = DepthRenderer()
    private val screenRenderer: ScreenRenderer = ScreenRenderer()
    private val mainRenderer: MainRenderer = MainRenderer()

    internal var depthLayer: RenderLayer = RenderLayerFactory(Application.window.width, Application.window.height, RenderLayerType.DEPTH)

    const val LIGHT_AMBIENT_COUNT = 20
    const val LIGHT_POINT_COUNT = 20
    const val LIGHT_DIRECTIONAL_COUNT = 20

    internal fun init() {
        depthRenderer.init()
        screenRenderer.init()
        mainRenderer.init()

        depthLayer.init()
        EventManager.registerListener(EventListener.create<WindowResizeEvent> { event ->
            depthLayer.dispose()
            depthLayer = RenderLayerFactory(event.width, event.height).apply(RenderLayer::init)
        })
    }

    internal fun render(scene: SceneInstance) {
        depthRenderer.render(scene)
        mainRenderer.render(scene)
        screenRenderer.render(scene)
    }

    fun registerShader(name: String, shader: Shader) {
        shaders[name] = shader
    }

    override fun dispose() {
        // TODO("Dispose shaders")
    }
}