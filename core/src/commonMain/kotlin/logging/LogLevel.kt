package dev.pengie.kotaro.logging

/**
 * The higher the level the higher priority.
 */
object LogLevel {
    val Info = 0
    val Warning = 1
    val Error = 2
    val Fatal = 3

    internal fun getLevelName(level: Int) = when(level) {
        Info -> "Info"
        Warning -> "Warning"
        Error -> "Error"
        Fatal -> "Fatal"
        else -> "???"
    }
}