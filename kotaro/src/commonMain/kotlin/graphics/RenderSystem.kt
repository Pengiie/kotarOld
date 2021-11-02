package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.graphics.shader.Shader
import dev.pengie.kotaro.graphics.shader.ShaderFactory
import dev.pengie.kotaro.graphics.shader.Shaders
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.math.toMatrix4f
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.scene.components.light.AmbientLight
import dev.pengie.kotaro.scene.components.light.DirectionalLight
import dev.pengie.kotaro.scene.components.light.PointLight
import dev.pengie.kotaro.types.Disposable


internal object RenderSystem : Disposable {

    private val renderers: MutableList<Renderer> = mutableListOf()
    private val screenRenderer: ScreenRenderer = ScreenRenderer()

    private var renderer: BatchRenderer<Model>? = null
    private var shader: Shader? = null
    private var screenShader: Shader? = null

    const val LIGHT_AMBIENT_COUNT = 20
    const val LIGHT_POINT_COUNT = 20
    const val LIGHT_DIRECTIONAL_COUNT = 20

    internal fun init() {
        renderer = RendererFactory()
        shader = ShaderFactory.createShader(Shaders.Main).apply(Shader::init)
        screenShader = ShaderFactory.createShader(Shaders.Screen).apply(Shader::init)

        screenRenderer.init()
    }

    internal fun render(scene: SceneInstance) {
        shader?.start()

        for((i, lightEntity) in scene.createView(AmbientLight::class).withIndex()) {
            if(i >= LIGHT_AMBIENT_COUNT)
                break
            val light = scene.getComponent<AmbientLight>(lightEntity)!!
            val color = Vector3f(light.color.r.toFloat() / 255f, light.color.g.toFloat() / 255f, light.color.b.toFloat() / 255f)
            shader?.uniformVector3f("ambientLights[$i].color", color)
            shader?.uniformFloat("ambientLights[$i].strength", light.strength)
        }

        for((i, lightEntity) in scene.createView(PointLight::class, Transform::class).withIndex()) {
            if(i >= LIGHT_POINT_COUNT)
                break
            val light = scene.getComponent<PointLight>(lightEntity)!!
            val transform = scene.getComponent<Transform>(lightEntity)!!
            val color = Vector3f(light.color.r.toFloat() / 255f, light.color.g.toFloat() / 255f, light.color.b.toFloat() / 255f)
            shader?.uniformVector3f("pointLights[$i].position", transform.position)
            shader?.uniformVector3f("pointLights[$i].color", color)
            shader?.uniformFloat("pointLights[$i].strength", light.strength)
        }

        for((i, lightEntity) in scene.createView(DirectionalLight::class, Transform::class).withIndex()) {
            if(i >= LIGHT_DIRECTIONAL_COUNT)
                break
            val light = scene.getComponent<DirectionalLight>(lightEntity)!!
            val transform = scene.getComponent<Transform>(lightEntity)!!
            val color = Vector3f(light.color.r.toFloat() / 255f, light.color.g.toFloat() / 255f, light.color.b.toFloat() / 255f)
            shader?.uniformVector3f("directionalLights[$i].direction", transform.rotation.rotate(Vector3f.forward))
            shader?.uniformVector3f("directionalLights[$i].color", color)
            shader?.uniformFloat("directionalLights[$i].strength", light.strength)
        }

        for(cameraEntity in scene.createView(Transform::class, Camera::class)) {
            val cameraTransform = scene.getComponent<Transform>(cameraEntity)!!
            val camera = scene.getComponent<Camera>(cameraEntity)!!
            val renderLayer by camera::renderLayer

            renderLayer.bind()

            renderer?.viewport(Application.window.width, Application.window.height)

            renderer?.clearColor(camera.backgroundColor)
            renderer?.clearScreen()

            shader?.uniformMatrix4f("projViewMat",
                (camera.projectionMatrix * cameraTransform.toViewMatrix()).toMatrix4f())

            for (entity in scene.createView(Transform::class, MeshFilter::class, MeshRenderer::class)) {
                renderer?.begin()
                val transform = scene.getComponent<Transform>(entity)!!
                val meshFilter = scene.getComponent<MeshFilter>(entity)!!
                val meshRenderer = scene.getComponent<MeshRenderer>(entity)!!

                shader?.uniformMatrix4f("modelMat", transform.toModelMatrix())

                val hasTexture = meshRenderer.material.texture != null
                shader?.uniformBool("material.doLighting", meshRenderer.material.lighting)
                shader?.uniformBool("material.hasTexture", hasTexture)
                shader?.uniformColor("material.color", meshRenderer.material.color)
                if(hasTexture)
                    shader?.uniformTexture("material.texture", meshRenderer.material.texture!!)


                renderer?.render(ModelLibrary.getModel(meshFilter.mesh), meshRenderer)
                renderer?.end()
            }

            renderLayer.unbind()
        }

        shader?.stop()

        screenRenderer.render(scene)
    }

    override fun dispose() {
        // TODO("Dispose shaders")
    }
}