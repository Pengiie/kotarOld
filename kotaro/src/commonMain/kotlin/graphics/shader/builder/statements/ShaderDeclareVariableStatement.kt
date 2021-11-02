package dev.pengie.kotaro.graphics.shader.builder.statements

import dev.pengie.kotaro.graphics.shader.builder.ShaderExpression
import dev.pengie.kotaro.graphics.shader.builder.ShaderVariable

class ShaderDeclareVariableStatement(variable: ShaderVariable, exp: ShaderExpression) : ShaderAssignStatement(variable, exp)