package dev.pengie.kotaro.math

class Vector2i(x: Int, y: Int) : Vector2<Int>(x, y, ArithmeticInt) {
    constructor() : this(0)
    constructor(value: Int) : this(value, value)

    override fun copy(x: Int, y: Int): Vector2<Int> = Vector2i(x, y)
}