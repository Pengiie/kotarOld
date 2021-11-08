package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.Time
import dev.pengie.kotaro.graphics.shader.Shader
import dev.pengie.kotaro.graphics.shader.ShaderFactory
import dev.pengie.kotaro.graphics.shader.Shaders
import dev.pengie.kotaro.math.Vector2f
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.math.toMatrix4f
import dev.pengie.kotaro.math.toVector3f
import dev.pengie.kotaro.scene.Entity
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.scene.components.light.AmbientLight
import dev.pengie.kotaro.scene.components.light.DirectionalLight
import dev.pengie.kotaro.scene.components.light.PointLight

class MainRenderer : Renderer {
    private lateinit var renderer: BatchRenderer<Model>
    private lateinit var shader: Shader

    override fun init() {
        renderer = RendererFactory()
        shader = ShaderFactory.createShader(Shaders.Main).apply(Shader::init)
    }

    override fun render(scene: SceneInstance) {
        val map: HashMap<Shader, HashSet<Entity>> = hashMapOf()
        scene.createView(Transform::class, MeshFilter::class, MeshRenderer::class).forEach {
            val meshRenderer = scene.getComponent<MeshRenderer>(it)!!
            val shader: Shader =
                if(meshRenderer.shader == null)
                    this.shader
                else
                    RenderSystem.shaders[meshRenderer.shader]!!
            if(!map.containsKey(shader))
                map[shader] = hashSetOf()
            map[shader]!!.add(it)
        }
        for(cameraEntity in scene.createView(Transform::class, Camera::class)) {
            val cameraTransform = scene.getComponent<Transform>(cameraEntity)!!
            val camera = scene.getComponent<Camera>(cameraEntity)!!
            val renderLayer by camera::renderLayer

            renderLayer.bind()

            renderer.viewport(Application.window.width, Application.window.height)

            renderer.clearColor(camera.backgroundColor)
            renderer.clearScreen()

            map.forEach { (shader, entities) ->
                shader.start()

                shader.uniformMatrix4f("projViewMat",
                    (camera.projectionMatrix * cameraTransform.toViewMatrix()).toMatrix4f())
                shader.uniformFloat("deltaTime", Time.deltaTime)
                shader.uniformFloat("time", Time.time)
                shader.uniformTexture("depthTexture", RenderSystem.depthLayer.getTexture())
                shader.uniformVector2f("screenResolution", Vector2f(Application.window.width.toFloat(), Application.window.height.toFloat()))

                for ((i, lightEntity) in scene.createView(AmbientLight::class).withIndex()) {
                    if (i >= RenderSystem.LIGHT_AMBIENT_COUNT)
                        break
                    val light = scene.getComponent<AmbientLight>(lightEntity)!!
                    val color = Vector3f(
                        light.color.r.toFloat() / 255f,
                        light.color.g.toFloat() / 255f,
                        light.color.b.toFloat() / 255f
                    )
                    shader.uniformVector3f("ambientLights[$i].color", color)
                    shader.uniformFloat("ambientLights[$i].strength", light.strength)
                }

                for ((i, lightEntity) in scene.createView(PointLight::class, Transform::class).withIndex()) {
                    if (i >= RenderSystem.LIGHT_POINT_COUNT)
                        break
                    val light = scene.getComponent<PointLight>(lightEntity)!!
                    val transform = scene.getComponent<Transform>(lightEntity)!!
                    val color = Vector3f(
                        light.color.r.toFloat() / 255f,
                        light.color.g.toFloat() / 255f,
                        light.color.b.toFloat() / 255f
                    )
                    shader.uniformVector3f("pointLights[$i].position", transform.position)
                    shader.uniformVector3f("pointLights[$i].color", color)
                    shader.uniformFloat("pointLights[$i].strength", light.strength)
                }

                for ((i, lightEntity) in scene.createView(DirectionalLight::class, Transform::class).withIndex()) {
                    if (i >= RenderSystem.LIGHT_DIRECTIONAL_COUNT)
                        break
                    val light = scene.getComponent<DirectionalLight>(lightEntity)!!
                    val transform = scene.getComponent<Transform>(lightEntity)!!
                    val color = Vector3f(
                        light.color.r.toFloat() / 255f,
                        light.color.g.toFloat() / 255f,
                        light.color.b.toFloat() / 255f
                    )
                    shader.uniformVector3f(
                        "directionalLights[$i].direction",
                        (transform.rotation.rotate(Vector3f.forward) * -1f).toVector3f()
                    )
                    shader.uniformVector3f("directionalLights[$i].color", color)
                    shader.uniformFloat("directionalLights[$i].strength", light.strength)
                }

                entities.forEach { entity ->
                    renderer.begin()
                    val transform = scene.getComponent<Transform>(entity)!!
                    val meshFilter = scene.getComponent<MeshFilter>(entity)!!
                    val meshRenderer = scene.getComponent<MeshRenderer>(entity)!!

                    shader.uniformMatrix4f("modelMat", transform.toModelMatrix())

                    val hasTexture = meshRenderer.material.texture != null
                    shader.uniformBool("material.doLighting", meshRenderer.material.lighting)
                    shader.uniformBool("material.hasTexture", hasTexture)
                    shader.uniformColor("material.color", meshRenderer.material.color)
                    if(hasTexture)
                        shader.uniformTexture("material.texture", meshRenderer.material.texture!!)

                    renderer.render(ModelLibrary.getModel(meshFilter.mesh), meshRenderer)
                    renderer.end()
                }

                shader.stop()
            }

            renderLayer.unbind()
        }
    }

}