package dev.pengie.kotaro.math

abstract class Vector2<T : Number> (var x: T, var y: T, val arithmetic: ArithmeticType<T>) {
    constructor(value: T, arithmetic: ArithmeticType<T>) : this(value, value, arithmetic)

    fun x(x: T) = apply { this.x = x }
    fun y(y: T) = apply { this.y = y }

    fun copy(): Vector2<T> = copy(x, y)
    protected abstract fun copy(x: T, y: T): Vector2<T>

    operator fun plus(other: Vector2<T>): Vector2<T> = copy(arithmetic.plus(x, other.x), arithmetic.plus(y, other.y));
    operator fun plus(value: T): Vector2<T> = copy(arithmetic.plus(x, value), arithmetic.plus(y, value))
    operator fun plusAssign(other: Vector2<T>) { x(arithmetic.plus(x, other.x)).y(arithmetic.plus(y, other.y)) }
    operator fun plusAssign(value: T) { x(arithmetic.plus(x, value)).y(arithmetic.plus(y, value)) }

    operator fun minus(other: Vector2<T>): Vector2<T> = copy(arithmetic.minus(x, other.x), arithmetic.minus(y, other.y));
    operator fun minus(value: T): Vector2<T> = copy(arithmetic.minus(x, value), arithmetic.minus(y, value))
    operator fun minusAssign(other: Vector2<T>) { x(arithmetic.minus(x, other.x)).y(arithmetic.minus(y, other.y)) }
    operator fun minusAssign(value: T) { x(arithmetic.minus(x, value)).y(arithmetic.minus(y, value)) }

    operator fun times(value: T): Vector2<T> = copy(arithmetic.times(x, value), arithmetic.times(y, value))
    operator fun timesAssign(value: T) { x(arithmetic.times(x, value)).y(arithmetic.times(y, value)) }

    override fun toString(): String {
        return "[$x, $y]"
    }
}