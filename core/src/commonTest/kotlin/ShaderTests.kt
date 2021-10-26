import dev.pengie.kotaro.graphics.shader.builder.shader
import dev.pengie.kotaro.graphics.shader.builder.ShaderVariableType
import dev.pengie.kotaro.logging.ConsoleLogger
import dev.pengie.kotaro.logging.LogLevel
import dev.pengie.kotaro.logging.LoggerRegistry
import dev.pengie.kotaro.logging.logInfo
import kotlin.test.Test

class ShaderTests {

    @Test
    fun simpleShaderTest() {
        LoggerRegistry.registerLogger(ConsoleLogger(LogLevel.Info))
        val shader = shader {
            vertexShader {
                val position = input("position", ShaderVariableType.VEC3)
                val textureCoord = input("textureCoord", ShaderVariableType.VEC2)

                val passTextureCoord = output("passTextureCoord", ShaderVariableType.VEC2)

                main {
                    passTextureCoord.equal { textureCoord }
                    position { vec4(position, 1.0f.exp) }
                }
            }
            fragmentShader {
                val textureCoord = input("passTextureCoord", ShaderVariableType.VEC2)

                val outColor = output("outColor", ShaderVariableType.VEC4)

                val texture = uniform("texture", ShaderVariableType.SAMPLER2D)

                main {
                    outColor.equal { texture(texture, textureCoord) }
                }
            }
        }
    }
}