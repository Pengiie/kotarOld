package dev.pengie.kotaro.math

class Vector3f(x: Float, y: Float, z: Float) : Vector3<Float>(x, y, z, ArithmeticFloat) {
    constructor() : this(0f)
    constructor(value: Float) : this(value, value, value)

    override fun copy(x: Float, y: Float, z: Float): Vector3<Float> = Vector3f(x, y, z)

    //override fun plus(left: Float, right: Float): Float = left + right
    //override fun minus(left: Float, right: Float): Float = left - right
    //override fun times(left: Float, right: Float): Float = left * right

    override fun toString(): String = "[$x, $y, $z]"

    companion object {
        val forward = Vector3f(0f, 0f, -1f)
        val right = Vector3f(1f, 0f, 0f)
        val up = Vector3f(0f, 1f, 0f)
    }
}

fun Vector3<Float>.toVector3f(): Vector3f = this as Vector3f