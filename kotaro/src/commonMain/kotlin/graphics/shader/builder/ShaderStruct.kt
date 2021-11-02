package dev.pengie.kotaro.graphics.shader.builder

class ShaderStruct(val name: String, val members: List<ShaderVariable>) {
    val type
        get() = ShaderVariableType(this)
}