package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.builder.statements.ShaderStatement

class ShaderFunction(val name: String, val returnType: ShaderVariableType, val parameters: List<ShaderVariable>, val statements: List<ShaderStatement>)