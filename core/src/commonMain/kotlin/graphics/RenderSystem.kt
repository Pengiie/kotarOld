package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowResizeEvent
import dev.pengie.kotaro.graphics.shader.Shader
import dev.pengie.kotaro.graphics.shader.ShaderFactory
import dev.pengie.kotaro.graphics.shader.Uniforms
import dev.pengie.kotaro.math.Matrix4f
import dev.pengie.kotaro.math.toMatrix4f
import dev.pengie.kotaro.scene.Entity
import dev.pengie.kotaro.scene.Scene
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.types.Disposable

private val screenMesh = Mesh(
    vertices = mutableListOf(
        -1f, -1f, 0f,
         1f, -1f, 0f,
        -1f,  1f, 0f,
         1f,  1f, 0f
    ),
    textureCoords = mutableListOf(
        0f, 0f,
        1f, 0f,
        0f, 1f,
        1f, 1f
    ),
    indices = mutableListOf(
        0, 1, 2,
        2, 1, 3
    )
)

internal object RenderSystem : Disposable {

    private var renderer: BatchRenderer<Model>? = null
    private var shader: Shader? = null
    private var screenShader: Shader? = null

    internal fun init() {
        renderer = RendererFactory()
        shader = ShaderFactory.createMainShader().apply(Shader::init)
        screenShader = ShaderFactory.createScreenShader().apply(Shader::init)
    }

    internal fun render(scene: SceneInstance) {
        for(cameraEntity in scene.createView(Transform::class, Camera::class)) {
            val cameraTransform = scene.getComponent<Transform>(cameraEntity)!!
            val camera = scene.getComponent<Camera>(cameraEntity)!!
            val renderLayer by camera::renderLayer

            renderLayer.bind()

            renderer?.viewport(Application.window.width, Application.window.height)

            renderer?.clearColor(camera.backgroundColor)
            renderer?.clearScreen()

            shader?.start()

            shader?.uniformMatrix4f(Uniforms.PROJ_VIEW_MATRIX,
                (camera.projectionMatrix * cameraTransform.toViewMatrix()).toMatrix4f())

            for (entity in scene.createView(Transform::class, MeshFilter::class, MeshRenderer::class)) {
                renderer?.begin()
                val transform = scene.getComponent<Transform>(entity)!!
                val meshFilter = scene.getComponent<MeshFilter>(entity)!!
                val meshRenderer = scene.getComponent<MeshRenderer>(entity)!!

                shader?.uniformMatrix4f(Uniforms.MODEL_MATRIX, transform.toModelMatrix())

                renderer?.render(ModelLibrary.getModel(meshFilter.mesh), meshRenderer)
                renderer?.end()
            }

            shader?.stop()

            renderLayer.unbind()
        }

        renderer?.bindWindowLayer()
        renderer?.viewport(Application.window.width, Application.window.height)
        renderer?.clearColor(Color(0x000000FF))
        renderer?.clearScreen()
        screenShader?.start()

        if(scene.mainCamera != null) {
            val camera = scene.getComponent<Camera>(scene.mainCamera!!)!!
            val layer = camera.renderLayer
            val texture = layer.getTexture()

            texture.bind()
            renderer?.begin()
            renderer?.render(ModelLibrary.getModel(screenMesh), null)
            renderer?.end()
            texture.unbind()
        }
        screenShader?.stop()
    }

    override fun dispose() {
        // TODO("Dispose shaders")
    }
}