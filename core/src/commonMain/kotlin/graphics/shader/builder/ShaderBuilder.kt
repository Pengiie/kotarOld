package dev.pengie.kotaro.graphics.shader.builder

class ShaderBuilder {
    private var vertexShader: VertexShaderBuilder? = null

    fun vertexShader(shader: VertexShaderBuilder.() -> Unit) {
        this.vertexShader = VertexShaderBuilder().apply(shader)
    }

    fun validate(): Boolean {
        if(this.vertexShader == null) {

        }
        return true
    }
}

fun shader(shader: ShaderBuilder.() -> Unit): ShaderBuilder {
    val builder = ShaderBuilder().apply(shader)
    builder.validate()
    return builder
}