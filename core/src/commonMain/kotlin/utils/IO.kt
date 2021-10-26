package dev.pengie.kotaro.utils

import dev.pengie.kotaro.assets.Path

expect object IO {
    fun readBytes(path: Path): ByteArray
}