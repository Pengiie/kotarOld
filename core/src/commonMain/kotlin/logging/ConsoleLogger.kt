package dev.pengie.kotaro.logging

class ConsoleLogger(logLevel: Int) : Logger(logLevel) {
    override fun log(level: Int, message: String) {
        if(!shouldLog(level))
            return
        println("[${LogLevel.getLevelName(level)}] $message")
    }
}