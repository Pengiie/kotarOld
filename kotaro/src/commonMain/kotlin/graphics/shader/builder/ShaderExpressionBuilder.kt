package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.builder.expressions.*

class ShaderExpressionBuilder {
    fun vec3(x: ShaderExpression, y: ShaderExpression, z: ShaderExpression, ) = ShaderVec3(x, y, z)
    fun vec3(exp: ShaderExpression) = ShaderVec3Exp(exp)
    fun vec4(vec3: ShaderExpression, w: ShaderExpression) = ShaderSwizzleVec4(vec3, w)
    fun vec4(x: ShaderExpression, y: ShaderExpression, z: ShaderExpression, w: ShaderExpression): ShaderVec4 = ShaderVec4(x, y, z, w)

    fun fragPos() = ShaderFragPos()

    fun max(left: ShaderExpression, right: ShaderExpression) = ShaderMax(left, right)

    fun dot(left: ShaderExpression, right: ShaderExpression) = ShaderDot(left, right)
    fun normalize(vec: ShaderExpression) = ShaderNormalize(vec)
    fun length(vec: ShaderExpression) = ShaderLength(vec)

    fun paren(exp: ShaderExpression) = ShaderParenthesis(exp)

    fun mix(a: ShaderExpression, b: ShaderExpression, weight: ShaderExpression) = ShaderMix(a, b, weight)

    operator fun ShaderExpression.not() = ShaderUnary(this)
    operator fun ShaderExpression.div(right: ShaderExpression) = ShaderDivide(this, right)
    operator fun ShaderExpression.times(right: ShaderExpression) = ShaderTimes(this, right)
    operator fun ShaderExpression.minus(right: ShaderExpression) = ShaderMinus(this, right)
    operator fun ShaderExpression.plus(right: ShaderExpression) = ShaderPlus(this, right)

    fun call(function: ShaderFunction, vararg args: ShaderExpression) = ShaderFunctionCall(function, *args)

    fun texture(sampler: ShaderExpression, coordinate: ShaderExpression) = ShaderTexture(sampler, coordinate)
    fun sin(exp: ShaderExpression) = ShaderSin(exp)
    fun cos(exp: ShaderExpression) = ShaderCos(exp)
}