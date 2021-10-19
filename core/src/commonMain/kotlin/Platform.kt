package dev.pengie.kotaro

enum class Platform {
    WINDOWS,
    UNKNOWN;

    fun exists() = this != UNKNOWN
}