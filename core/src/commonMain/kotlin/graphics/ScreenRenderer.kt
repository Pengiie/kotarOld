package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.graphics.shader.Shader
import dev.pengie.kotaro.graphics.shader.ShaderFactory
import dev.pengie.kotaro.graphics.shader.builder.ShaderVariablePrimitiveType
import dev.pengie.kotaro.graphics.shader.builder.shader
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.Camera

class ScreenRenderer : Renderer {
    private lateinit var renderer: BatchRenderer<Model>
    private lateinit var shader: Shader

    override fun init() {
        renderer = RendererFactory()
        shader = ShaderFactory.createShader(screenShader).apply(Shader::init)
    }

    override fun render(scene: SceneInstance) {
        renderer.bindWindowLayer()
        renderer.viewport(Application.window.width, Application.window.height)
        renderer.clearColor(Color(0x000000FF))
        renderer.clearScreen()
        shader.start()

        if(scene.mainCamera != null) {
            val camera = scene.getComponent<Camera>(scene.mainCamera!!)!!
            val layer = camera.renderLayer
            val texture = layer.getTexture()

            shader.uniformTexture("texture", texture)
            renderer.begin()
            renderer.render(ModelLibrary.getModel(screenMesh), null)
            renderer.end()
            texture.unbind()
        }
        shader.stop()
    }
}

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

private val screenShader = shader {
    vertexShader {
        val position = input(0, "position", ShaderVariablePrimitiveType.VEC3)
        val textureCoord = input(1, "textureCoord", ShaderVariablePrimitiveType.VEC2)

        val passTextureCoord = output("passTextureCoord", ShaderVariablePrimitiveType.VEC2)

        main {
            passTextureCoord.equal { textureCoord }
            position { vec4(position, 1.0f.exp) }
        }
    }
    fragmentShader {
        val textureCoord = input("passTextureCoord", ShaderVariablePrimitiveType.VEC2)

        val outColor = output("outColor", ShaderVariablePrimitiveType.VEC4)

        val texture = uniform("texture", ShaderVariablePrimitiveType.SAMPLER2D)

        main {
            outColor.equal { texture(texture, textureCoord) }
        }
    }
}