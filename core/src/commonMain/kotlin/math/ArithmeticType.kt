package dev.pengie.kotaro.math

interface ArithmeticType<T : Number> {
    fun plus(left: T, right: T): T
    fun minus(left: T, right: T): T
    fun times(left: T, right: T): T
    fun negative(value: T): T
}