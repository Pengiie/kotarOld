package dev.pengie.kotaro

expect object ApplicationConfigFactory {
    fun getConfig(): ApplicationConfig
}

interface ApplicationConfig {
    var platform: Platform
    var width: Int
    var height: Int
}