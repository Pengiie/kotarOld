package dev.pengie.kotaro.graphics.shader.builder

import dev.pengie.kotaro.graphics.shader.ShaderException
import dev.pengie.kotaro.logging.logFatalAndThrow

open class ShaderVariable(val name: String, val type: ShaderVariableType) : ShaderExpression {
    operator fun get(property: String): ShaderVariable {
        if(this.type.type != ShaderVariablePrimitiveType.STRUCT)
            logFatalAndThrow(ShaderException("Variable $name could not access member of a non-struct"))
        val propertyType = this.type.struct!!.members.find { it.name == property }
        if(propertyType == null)
            logFatalAndThrow(ShaderException("Property $property doesn't exist for struct ${this.type.struct!!.name}"))
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