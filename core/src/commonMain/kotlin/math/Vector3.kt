package dev.pengie.kotaro.math

abstract class Vector3<T : Number> (var x: T, var y: T, var z: T, private val arithmetic: ArithmeticType<T>) {
    constructor(value: T, arithmetic: ArithmeticType<T>) : this(value, value, value, arithmetic)

    fun x(x: T) = apply { this.x = x }
    fun y(y: T) = apply { this.y = y }
    fun z(z: T) = apply { this.z = z }

    fun copy(): Vector3<T> = copy(x, y, z)
    protected abstract fun copy(x: T, y: T, z: T): Vector3<T>

    operator fun plus(other: Vector3<T>): Vector3<T> = copy(arithmetic.plus(x, other.x), arithmetic.plus(y, other.y), arithmetic.plus(z, other.z));
    operator fun plus(value: T): Vector3<T> = copy(arithmetic.plus(x, value), arithmetic.plus(y, value), arithmetic.plus(z, value))
    operator fun plusAssign(other: Vector3<T>) { x(arithmetic.plus(x, other.x)).y(arithmetic.plus(y, other.y)).z(arithmetic.plus(z, other.z)) }
    operator fun plusAssign(value: T) { x(arithmetic.plus(x, value)).y(arithmetic.plus(y, value)).z(arithmetic.plus(z, value)) }

    operator fun minus(other: Vector3<T>): Vector3<T> = copy(arithmetic.minus(x, other.x), arithmetic.minus(y, other.y), arithmetic.minus(z, other.z));
    operator fun minus(value: T): Vector3<T> = copy(arithmetic.minus(x, value), arithmetic.minus(y, value), arithmetic.minus(z, value))
    operator fun minusAssign(other: Vector3<T>) { x(arithmetic.minus(x, other.x)).y(arithmetic.minus(y, other.y)).z(arithmetic.minus(z, other.z)) }
    operator fun minusAssign(value: T) { x(arithmetic.minus(x, value)).y(arithmetic.minus(y, value)).z(arithmetic.minus(z, value)) }

    operator fun times(value: T): Vector3<T> = copy(arithmetic.times(x, value), arithmetic.times(y, value), arithmetic.times(z, value))
    operator fun timesAssign(value: T) { x(arithmetic.times(x, value)).y(arithmetic.times(y, value)).z(arithmetic.times(z, value)) }

    operator fun unaryMinus(): Vector3<T> = copy(arithmetic.negative(x), arithmetic.negative(y), arithmetic.negative(z))
}