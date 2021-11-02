package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.ShaderException
import dev.pengie.kotaro.graphics.shader.builder.expressions.ShaderFloat
import dev.pengie.kotaro.logging.logFatalAndThrow

class ShaderBuilder {
    internal var vertexShader: VertexShaderBuilder? = null
    internal var fragmentShader: FragmentShaderBuilder? = null

    fun vertexShader(shader: VertexShaderBuilder.() -> Unit) {
        this.vertexShader = VertexShaderBuilder().apply(shader)
    }
    fun fragmentShader(shader: FragmentShaderBuilder.() -> Unit) {
        this.fragmentShader = FragmentShaderBuilder().apply(shader)
    }

    inline val Float.exp: ShaderFloat get() = ShaderFloat(this)

    fun validate(): Boolean {
        if(this.vertexShader == null)
            logFatalAndThrow(ShaderException("Shader requires vertex shader"))
        if(this.fragmentShader == null)
            logFatalAndThrow(ShaderException("Shader requires fragment shader"))
        return true
    }
}

fun shader(shader: ShaderBuilder.() -> Unit): ShaderBuilder {
    val builder = ShaderBuilder().apply(shader)
    builder.validate()
    return builder
}