package dev.pengie.kotaro.graphics.shader.builder.expressions

import dev.pengie.kotaro.graphics.shader.builder.ShaderExpression

class ShaderVec3(val x: ShaderExpression, val y: ShaderExpression, val z: ShaderExpression) : ShaderExpression

class ShaderVec3Exp(val exp: ShaderExpression) : ShaderExpression