package dev.pengie.kotaro.math

import kotlin.math.PI

const val TO_DEGREE = (180f / PI).toFloat()
fun Float.toDegrees(): Float = this * TO_DEGREE

const val TO_RADIAN = (PI / 180f).toFloat()
fun Float.toRadians(): Float = this * TO_RADIAN