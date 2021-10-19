package dev.pengie.kotaro.math

abstract class Matrix3<T : Number>(zeroValue: T, private val arithmetic: ArithmeticType<T>) {
    var m00 = zeroValue
    var m01 = zeroValue
    var m02 = zeroValue

    var m10 = zeroValue
    var m11 = zeroValue
    var m12 = zeroValue

    var m20 = zeroValue
    var m21 = zeroValue
    var m22 = zeroValue

    abstract fun copy(): Matrix3<T>

    abstract fun toArray(): Array<T>

    abstract operator fun times(other: Matrix3<T>): Matrix3<T>
    abstract operator fun times(other: Vector3<T>): Vector3<T>
}