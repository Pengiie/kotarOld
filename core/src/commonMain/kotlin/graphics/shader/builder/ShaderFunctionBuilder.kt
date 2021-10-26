package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.builder.ShaderVariablePrimitiveType.*
import dev.pengie.kotaro.graphics.shader.builder.statements.*

class ShaderFunctionBuilder(val name: String, val returnType: ShaderVariableType) : ShaderBlockBuilder() {
    private val parameters: MutableList<ShaderVariable> = mutableListOf()

    fun parameter(name: String, type: ShaderVariableType): ShaderVariable = ShaderVariable(name, type).apply(parameters::add)
    fun parameter(name: String, type: ShaderVariablePrimitiveType): ShaderVariable = parameter(name, ShaderVariableType(type))

    fun build(): ShaderFunction = ShaderFunction(name, returnType, parameters, statements)
}