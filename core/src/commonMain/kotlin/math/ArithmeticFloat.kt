package dev.pengie.kotaro.math

object ArithmeticFloat : ArithmeticType<Float> {
    override fun plus(left: Float, right: Float): Float = left + right
    override fun minus(left: Float, right: Float): Float = left - right
    override fun times(left: Float, right: Float): Float = left * right
    override fun negative(value: Float): Float = -value
}