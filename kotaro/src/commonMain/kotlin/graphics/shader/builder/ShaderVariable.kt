package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.ShaderException
import dev.pengie.kotaro.logging.logFatalAndThrow

open class ShaderVariable(val name: String, val type: ShaderVariableType) : ShaderExpression {
    operator fun get(property: String): ShaderVariable {
        val propertyType = when(this.type.type) {
            ShaderVariablePrimitiveType.STRUCT -> this.type.struct!!.members.find { it.name == property }
            ShaderVariablePrimitiveType.VEC4 -> ShaderVariable(property, ShaderVariableType(ShaderVariablePrimitiveType.FLOAT))
            ShaderVariablePrimitiveType.VEC3 -> ShaderVariable(property, ShaderVariableType(ShaderVariablePrimitiveType.FLOAT))
            ShaderVariablePrimitiveType.VEC2 -> ShaderVariable(property, ShaderVariableType(ShaderVariablePrimitiveType.FLOAT))
            else -> null
        }
        if(propertyType == null)
            logFatalAndThrow(ShaderException("Property $property doesn't exist for variable ${this.type.struct!!.name}"))
        return ShaderVariable("$name.$property", propertyType!!.type)
    }

    fun ensureType(type: ShaderVariableType) {
        if(this.type != type)
            logFatalAndThrow(ShaderException("Mismatched types when using $name, expected $type but got ${this.type}"))
    }
}

class ShaderVariableType(val type: ShaderVariablePrimitiveType) {
    internal var struct: ShaderStruct? = null
        private set
    constructor(struct: ShaderStruct) : this(ShaderVariablePrimitiveType.STRUCT) {
        this.struct = struct
    }
}

enum class ShaderVariablePrimitiveType {
    VOID,

    BOOL,
    FLOAT,
    INT,
    VEC2,
    VEC3,
    VEC4,

    MAT4,
    SAMPLER2D,


    STRUCT
}