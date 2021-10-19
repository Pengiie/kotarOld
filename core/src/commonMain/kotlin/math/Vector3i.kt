package dev.pengie.kotaro.math

class Vector3i(x: Int, y: Int, z: Int) : Vector3<Int>(x, y, z, ArithmeticInt) {
    constructor() : this(0)
    constructor(value: Int) : this(value, value, value)

    override fun copy(x: Int, y: Int, z: Int): Vector3<Int> = Vector3i(x, y, z)
}