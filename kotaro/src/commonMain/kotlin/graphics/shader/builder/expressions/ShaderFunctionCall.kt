package dev.pengie.kotaro.graphics.shader.builder.expressions

import dev.pengie.kotaro.graphics.shader.builder.ShaderExpression
import dev.pengie.kotaro.graphics.shader.builder.ShaderFunction

class ShaderFunctionCall(val function: ShaderFunction, vararg args: ShaderExpression) : ShaderExpression {
    val args = args.toList()
}