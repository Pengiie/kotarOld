package dev.pengie.kotaro.math

class Vector2f(x: Float, y: Float) : Vector2<Float>(x, y, ArithmeticFloat) {
    constructor() : this(0f)
    constructor(value: Float) : this(value, value)

    override fun copy(x: Float, y: Float): Vector2<Float> = Vector2f(x, y)
}