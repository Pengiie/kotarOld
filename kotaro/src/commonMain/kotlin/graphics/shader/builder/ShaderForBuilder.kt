package dev.pengie.kotaro.graphics.shader.builder

class ShaderForBuilder(name: String) : ShaderBlockBuilder() {
    val i = ShaderVariable(name, ShaderVariableType(ShaderVariablePrimitiveType.INT))
}