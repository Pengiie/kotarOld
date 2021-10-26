package dev.pengie.kotaro.utils

import dev.pengie.kotaro.assets.Path
import java.io.IOException

actual object IO {
    actual fun readBytes(path: Path): ByteArray {
        try {
            (IO::class as Any).javaClass.classLoader.getResourceAsStream(path.fullPath).use {
                if(it == null)
                    throw Exception("Could not load file ${path.fullPath}")
                val arr = ByteArray(it.available())
                it.read(arr)
                return arr
            }
        } catch (exception: IOException) {
            throw Exception("Could not load file ${path.fullPath}")
        }
    }

}