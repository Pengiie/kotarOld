package dev.pengie.kotaro.graphics.shader.builder

class ShaderVariableArray(name: String, type: ShaderVariableType, val size: Int) : ShaderVariable(name, type) {
    operator fun get(index: ShaderVariable): ShaderVariable = ShaderVariable("$name[${index.name}]", type)
}