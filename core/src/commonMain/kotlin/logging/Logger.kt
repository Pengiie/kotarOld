package dev.pengie.kotaro.logging

abstract class Logger(private val level: Int) {
    abstract fun log(level: Int, message: String)
    fun shouldLog(level: Int) = level >= this.level
}

fun log(level: Int, message: String) {
    LoggerRegistry.loggers.forEach { it.log(level, message) }
}

fun logInfo(message: String) = log(LogLevel.Info, message)
fun logWarning(message: String) = log(LogLevel.Warning, message)
fun logError(message: String) = log(LogLevel.Error, message)
fun logFatal(message: String) = log(LogLevel.Fatal, message)

fun logFatalAndThrow(error: Error) {
    error.message?.let(::logFatal)
    throw error
}