package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.builder.ShaderVariablePrimitiveType.*
import dev.pengie.kotaro.graphics.shader.builder.expressions.*
import dev.pengie.kotaro.graphics.shader.builder.statements.*

fun CommonShaderBuilder.toSource(): String {
    val builder = StringBuilder("#version 400 core\n")
    this.structs.forEach {
        builder.append("struct ${it.name} {\n")
        it.members.forEach { member ->
            builder.append("${member.type.toGLSL()} ${member.name};\n")
        }
        builder.append("};\n")
    }
    this.inputs.forEach {
        if(it is ShaderVariableInput)
            builder.append("layout(location = ${it.attribLocation}) in ${it.type.toGLSL()} ${it.name};\n")
        else
            builder.append("in ${it.type.toGLSL()} ${it.name};\n")
    }
    this.outputs.forEach {
        builder.append("out ${it.type.toGLSL()} ${it.name};\n")
    }
    this.uniforms.forEach {
        if(it is ShaderVariableArray)
            builder.append("uniform ${it.type.toGLSL()} ${it.name}[${it.size}];\n")
        else
            builder.append("uniform ${it.type.toGLSL()} ${it.name};\n")
    }
    this.functions.forEach {
        builder.append("${it.returnType.toGLSL()} ${it.name}(")
        it.parameters.forEachIndexed { index, param ->
            if(index != 0)
                builder.append(", ")
            builder.append("${param.type.toGLSL()} ${param.name}")
        }
        builder.append(") {\n")
        it.statements.forEach { statement ->
            builder.append("${ processStatement(statement)}\n")
        }
        builder.append("}\n")
    }
    return builder.toString()
}

private fun processStatement(statement: ShaderStatement): String = when(statement) {
    is ShaderReturnStatement -> "return ${processExpression(statement.exp)};"
    is ShaderDeclareVariableStatement -> "${statement.left.type.toGLSL()} ${statement.left.name} = ${processExpression(statement.right)};"
    is ShaderAssignStatement -> "${statement.left.name} = ${processExpression(statement.right)};"
    is ShaderForStatement -> {
        val builder = StringBuilder("for(int ${statement.countName} = 0; ${statement.countName} < ${statement.count}; ${statement.countName}++) {\n")
        statement.statements.forEach {
            builder.append("${processStatement(it)}\n")
        }
        builder.append("}")
        builder.toString()
    }
    is ShaderIfStatement -> {
        val builder = StringBuilder("if(${processExpression(statement.condition)}) {\n")
        statement.statements.forEach {
            builder.append("${processStatement(it)}\n")
        }
        builder.append("}")
        builder.toString()
    }
    else -> ""
}

private fun processExpression(exp: ShaderExpression): String = when(exp) {
    // Leaf expressions
    is ShaderFloat -> exp.value.toString()
    is ShaderVariable -> exp.name

    // Vector expressions
    is ShaderVec3 -> "vec3(${processExpression(exp.x)}, ${processExpression(exp.y)}, ${processExpression(exp.z)})"
    is ShaderVec3Exp -> "vec3(${ processExpression(exp.exp)})"
    is ShaderVec4 -> "vec4(${processExpression(exp.x)}, ${processExpression(exp.y)}, ${processExpression(exp.z)}, ${processExpression(exp.w)})"
    is ShaderSwizzleVec4 -> "vec4(${processExpression(exp.vec3)}, ${processExpression(exp.w)})"

    // Math
    is ShaderPlus -> "${processExpression(exp.left)} + ${processExpression(exp.right)}"
    is ShaderMinus -> "${processExpression(exp.left)} - ${processExpression(exp.right)}"
    is ShaderTimes -> "${processExpression(exp.left)} * ${processExpression(exp.right)}"
    is ShaderDivide -> "${processExpression(exp.left)} / ${processExpression(exp.right)}"
    is ShaderParenthesis -> "(${processExpression(exp.exp)})"
    is ShaderMax -> "max(${processExpression(exp.left)}, ${processExpression(exp.right)})"
    is ShaderDot -> "dot(${processExpression(exp.left)}, ${processExpression(exp.right)})"
    is ShaderLength -> "length(${processExpression(exp.vec)})"
    is ShaderNormalize -> "normalize(${processExpression(exp.vec)})"
    is ShaderUnary -> "!${processExpression(exp.exp)}"

    // Built Ins
    is ShaderFragPos -> "gl_FragCoord"

    // Functions
    is ShaderSin -> "sin(${processExpression(exp.exp)})"
    is ShaderCos -> "cos(${processExpression(exp.exp)})"
    is ShaderTexture -> "texture(${processExpression(exp.sampler)}, ${processExpression(exp.coordinate)})"
    is ShaderMix -> "mix(${processExpression(exp.a)}, ${processExpression(exp.b)}, ${processExpression(exp.weight)})"
    is ShaderFunctionCall -> {
        val builder = StringBuilder("${exp.function.name}(")
        exp.args.forEachIndexed { index, shaderExpression ->
            if(index != 0)
                builder.append(", ")
            builder.append(processExpression(shaderExpression))
        }
        builder.append(")")
        builder.toString()
    }
    else -> ""
}

fun ShaderVariableType.toGLSL(): String = when(this.type) {
    FLOAT -> "float"
    VOID -> "void"
    STRUCT -> this.struct!!.name
    BOOL -> "bool"
    INT -> "int"
    VEC2 -> "vec2"
    VEC3 -> "vec3"
    VEC4 -> "vec4"
    MAT4 -> "mat4"
    SAMPLER2D -> "sampler2D"
}