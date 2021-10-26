package dev.pengie.kotaro.graphics.shader.builder

class VertexShaderBuilder : CommonShaderBuilder() {
    fun input(attribLocation: Int, name: String, type: ShaderVariableType): ShaderVariable = ShaderVariableInput(attribLocation, "kot_$name", type).apply(inputs::add)
    fun input(attribLocation: Int, name: String, type: ShaderVariablePrimitiveType): ShaderVariable = input(attribLocation, name, ShaderVariableType(type))

}