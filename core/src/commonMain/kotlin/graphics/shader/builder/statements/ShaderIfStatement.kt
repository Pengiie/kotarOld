package dev.pengie.kotaro.graphics.shader.builder.statements

import dev.pengie.kotaro.graphics.shader.builder.ShaderExpression

class ShaderIfStatement(val condition: ShaderExpression, val statements: List<ShaderStatement>) : ShaderStatement