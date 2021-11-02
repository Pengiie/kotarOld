package dev.pengie.kotaro.logging

object LoggerRegistry {
    internal val loggers: MutableList<Logger> = mutableListOf()

    fun registerLogger(logger: Logger) {
        loggers.add(logger)
    }
}