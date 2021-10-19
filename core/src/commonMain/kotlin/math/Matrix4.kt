package dev.pengie.kotaro.math

abstract class Matrix4<T : Number>(zeroValue: T, private val arithmetic: ArithmeticType<T>) {
    var m00 = zeroValue
    var m01 = zeroValue
    var m02 = zeroValue
    var m03 = zeroValue

    var m10 = zeroValue
    var m11 = zeroValue
    var m12 = zeroValue
    var m13 = zeroValue

    var m20 = zeroValue
    var m21 = zeroValue
    var m22 = zeroValue
    var m23 = zeroValue

    var m30 = zeroValue
    var m31 = zeroValue
    var m32 = zeroValue
    var m33 = zeroValue

    abstract fun copy(): Matrix4<T>

    abstract fun toArray(): Array<T>

    abstract fun translate(x: Float, y: Float, z: Float)
    abstract fun rotateX(angle: Float)
    abstract fun rotateY(angle: Float)
    abstract fun rotateZ(angle: Float)
    fun rotate(x: Float, y: Float, z: Float) {
        rotateZ(x)
        rotateY(y)
        rotateX(z)
    }
    abstract fun scale(x: Float, y: Float, z: Float)
    abstract operator fun times(other: Matrix4<T>): Matrix4<T>
}