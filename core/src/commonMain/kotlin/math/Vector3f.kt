package dev.pengie.kotaro.math

import kotlin.math.sqrt

open class Vector3f(x: Float, y: Float, z: Float) : Vector3<Float>(x, y, z, ArithmeticFloat) {
    constructor() : this(0f)
    constructor(value: Float) : this(value, value, value)

    override fun copy(x: Float, y: Float, z: Float): Vector3<Float> = Vector3f(x, y, z)

    //override fun plus(left: Float, right: Float): Float = left + right
    //override fun minus(left: Float, right: Float): Float = left - right
    //override fun times(left: Float, right: Float): Float = left * right

    fun length(): Float =
        sqrt(x*x + y*y + z*z)

    fun normalize(): Vector3f = this.copy().apply {
        val length = length()
        x /= length
        y /= length
        z /= length
    }.toVector3f()

    override fun toString(): String = "[$x, $y, $z]"

    companion object {
        val forward = Vector3f(0f, 0f, -1f)
        val right = Vector3f(1f, 0f, 0f)
        val up = Vector3f(0f, 1f, 0f)

        fun dot(left: Vector3f, right: Vector3f): Float = left.x * right.x + left.y * right.y + left.z * right.z
        fun cross(left: Vector3f, right: Vector3f): Vector3f =
            Vector3f(
                left.y * right.z - left.z * right.y,
                left.z * right.x - left.x * right.z,
                left.x * right.y - left.y * right.x
            )
    }
}

fun Vector3<Float>.toVector3f(): Vector3f = this as Vector3f