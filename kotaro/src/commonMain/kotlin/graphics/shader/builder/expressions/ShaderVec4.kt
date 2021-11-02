package dev.pengie.kotaro.graphics.shader.builder.expressions

import dev.pengie.kotaro.graphics.shader.builder.ShaderExpression

class ShaderVec4(
    val x: ShaderExpression,
    val y: ShaderExpression,
    val z: ShaderExpression,
    val w: ShaderExpression) : ShaderExpression

class ShaderSwizzleVec4(
    val vec3: ShaderExpression,
    val w: ShaderExpression) : ShaderExpression