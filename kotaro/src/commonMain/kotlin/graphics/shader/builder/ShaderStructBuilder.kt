package dev.pengie.kotaro.graphics.shader.builder

class ShaderStructBuilder(val name: String) {
    val members: MutableList<ShaderVariable> = mutableListOf()

    fun member(name: String, type: ShaderVariableType) = members.add(ShaderVariable(name, type))
    fun member(name: String, type: ShaderVariablePrimitiveType) = member(name, ShaderVariableType(type))

    fun build(): ShaderStruct = ShaderStruct(name, members)
}