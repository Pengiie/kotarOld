package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.graphics.shader.Shader
import dev.pengie.kotaro.graphics.shader.ShaderFactory
import dev.pengie.kotaro.graphics.shader.Shaders
import dev.pengie.kotaro.graphics.shader.builder.ShaderVariablePrimitiveType
import dev.pengie.kotaro.graphics.shader.builder.shader
import dev.pengie.kotaro.math.toMatrix4f
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*

class DepthRenderer : Renderer {

    private lateinit var renderer: BatchRenderer<Model>
    private lateinit var shader: Shader

    override fun init() {
        renderer = RendererFactory()
        shader = ShaderFactory.createShader(shader {
            vertexShader {
                val position = input(0, "position", ShaderVariablePrimitiveType.VEC3)
                val projViewMatrix = uniform("projViewMat", ShaderVariablePrimitiveType.MAT4)
                val model = uniform("modelMat", ShaderVariablePrimitiveType.MAT4)

                main {
                    position { projViewMatrix * model * vec4(position, 1.0f.exp) }
                }
            }

            fragmentShader {
                main {

                }
            }
        }).apply(Shader::init)
    }

    override fun render(scene: SceneInstance) {
        if(scene.mainCamera != null) {
            RenderSystem.depthLayer.bind()
            renderer.viewport(Application.window.width, Application.window.height)
            renderer.clearScreen()
            shader.start()
            val cameraTransform = scene.getComponent<Transform>(scene.mainCamera!!)!!
            val camera = scene.getComponent<Camera>(scene.mainCamera!!)!!
            shader.uniformMatrix4f("projViewMat",
                (camera.projectionMatrix * cameraTransform.toViewMatrix()).toMatrix4f())
            scene.createView(Transform::class, MeshFilter::class, MeshRenderer::class).forEach {
                val meshRenderer = scene.getComponent<MeshRenderer>(it)!!
                if(meshRenderer.layer == DrawLayer.WATER)
                    return@forEach
                val transform = scene.getComponent<Transform>(it)!!
                shader.uniformMatrix4f("modelMat", transform.toModelMatrix())
                renderer.begin()
                renderer.render(
                    ModelLibrary.getModel(scene.getComponent<MeshFilter>(it)!!.mesh),
                    meshRenderer
                )
                renderer.end()
            }
            shader.stop()
            RenderSystem.depthLayer.unbind()
        }
    }

}