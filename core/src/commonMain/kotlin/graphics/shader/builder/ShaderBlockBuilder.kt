package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.builder.statements.*

open class ShaderBlockBuilder {
    val statements: MutableList<ShaderStatement> = mutableListOf()

    fun ShaderVariable.equal(expression: ShaderExpressionBuilder.() -> ShaderExpression) {
        statements.add(ShaderAssignStatement(this, expression.invoke(ShaderExpressionBuilder())))
    }

    fun variable(name: String, type: ShaderVariableType, exp: ShaderExpressionBuilder.() -> ShaderExpression): ShaderVariable =
        ShaderVariable(name, type).apply { statements.add(ShaderDeclareVariableStatement(this, exp.invoke(ShaderExpressionBuilder()))) }
    fun variable(name: String, type: ShaderVariablePrimitiveType, exp: ShaderExpressionBuilder.() -> ShaderExpression): ShaderVariable = variable(name, ShaderVariableType(type), exp)

    fun position(expression: ShaderExpressionBuilder.() -> ShaderExpression) {
        ShaderVariable("gl_Position", ShaderVariableType(ShaderVariablePrimitiveType.VEC4)).equal(expression)
    }

    fun expression(expression: ShaderExpressionBuilder.() -> ShaderExpression): ShaderExpression = expression.invoke(ShaderExpressionBuilder())

    fun runIf(condition: ShaderExpression, scope: ShaderBlockBuilder.() -> Unit) {
        statements.add(ShaderIfStatement(condition, ShaderBlockBuilder().apply(scope).statements))
    }

    fun forI(name: String, length: Int, scope: ShaderForBuilder.() -> Unit) {
        statements.add(ShaderForStatement(name, length, ShaderForBuilder(name).apply(scope).statements))
    }
}