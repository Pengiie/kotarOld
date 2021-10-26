package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.builder.statements.ShaderReturnStatement

open class CommonShaderBuilder {
    internal val inputs: MutableList<ShaderVariable> = mutableListOf()
    internal val outputs: MutableList<ShaderVariable> = mutableListOf()
    internal val uniforms: MutableList<ShaderVariable> = mutableListOf()
    internal val functions: MutableList<ShaderFunction> = mutableListOf()
    internal val structs: MutableList<ShaderStruct> = mutableListOf()

    fun input(name: String, type: ShaderVariableType): ShaderVariable = ShaderVariable("kot_$name", type).apply(inputs::add)
    fun input(name: String, type: ShaderVariablePrimitiveType): ShaderVariable = input(name, ShaderVariableType(type))

    fun output(name: String, type: ShaderVariableType): ShaderVariable = ShaderVariable("kot_$name", type).apply(outputs::add)
    fun output(name: String, type: ShaderVariablePrimitiveType): ShaderVariable = output(name, ShaderVariableType(type))

    fun uniform(name: String, type: ShaderVariableType): ShaderVariable = ShaderVariable("kotU_$name", type).apply(uniforms::add)
    fun uniform(name: String, type: ShaderVariablePrimitiveType): ShaderVariable = uniform(name, ShaderVariableType(type))

    fun uniform(name: String, type: ShaderVariableType, size: Int): ShaderVariableArray = ShaderVariableArray("kotU_$name", type, size).apply(uniforms::add)
    fun uniform(name: String, type: ShaderVariablePrimitiveType, size: Int): ShaderVariableArray = uniform(name, ShaderVariableType(type), size)

    fun struct(name: String, struct: ShaderStructBuilder.() -> Unit): ShaderStruct = ShaderStructBuilder(name).apply(struct).build().apply(structs::add)

    fun main(scope: ShaderFunctionBuilder.() -> Unit) = function("main", scope)
    fun function(name: String, returnType: ShaderVariableType, scope: ShaderFunctionBuilder.() -> ShaderExpression): ShaderFunction {
        val builder = ShaderFunctionBuilder(name, returnType)
        builder.statements.add(ShaderReturnStatement(scope.invoke(builder)))
        return builder.build().apply(functions::add)
    }
    fun function(name: String, returnType: ShaderVariablePrimitiveType, scope: ShaderFunctionBuilder.() -> ShaderExpression): ShaderFunction =
        function(name, ShaderVariableType(returnType), scope)
    fun function(name: String, scope: ShaderFunctionBuilder.() -> Unit): ShaderFunction {
        val builder = ShaderFunctionBuilder(name, ShaderVariableType(ShaderVariablePrimitiveType.VOID))
        scope.invoke(builder)
        return builder.build().apply(functions::add)
    }
}