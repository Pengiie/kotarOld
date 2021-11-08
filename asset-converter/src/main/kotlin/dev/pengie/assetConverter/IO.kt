package dev.pengie.assetConverter

import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

object IO {
    fun readBytes(path: Path): ByteArray {
        try {
            (IO::class as Any).javaClass.classLoader.getResourceAsStream(path.fullPath).use {
                val input: InputStream = it ?: return Files.readAllBytes(Paths.get(path.fullPath))
                val arr = ByteArray(input.available())
                input.read(arr)
                return arr
            }
        } catch (exception: IOException) {
            throw Exception("Could not load file ${path.fullPath}")
        }
    }

}