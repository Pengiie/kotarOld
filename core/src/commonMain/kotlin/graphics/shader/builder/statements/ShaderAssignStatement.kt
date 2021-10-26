package dev.pengie.kotaro.graphics.shader.builder.statements

import dev.pengie.kotaro.graphics.shader.builder.ShaderExpression
import dev.pengie.kotaro.graphics.shader.builder.ShaderVariable

open class ShaderAssignStatement(val left: ShaderVariable, val right: ShaderExpression) : ShaderStatement