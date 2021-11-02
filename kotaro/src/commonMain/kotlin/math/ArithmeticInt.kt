package dev.pengie.kotaro.math

object ArithmeticInt : ArithmeticType<Int> {
    override fun plus(left: Int, right: Int): Int = left + right
    override fun minus(left: Int, right: Int): Int = left - right
    override fun times(left: Int, right: Int): Int = left * right
    override fun negative(value: Int): Int = -value
}