package dev.pengie.kotaro.utils

import java.util.zip.Deflater
import java.util.zip.Inflater

actual object ZLib {
    actual fun compress(arr: ByteArray): ByteArray = Deflater().run {
        var out = ByteArray(1024)
        this.setInput(arr)
        this.finish()
        val size = this.deflate(out)
        out = out.copyOfRange(0, size - 1)
        this.end()
        return out
    }
    actual fun decompress(arr: ByteArray, expectedSize: Int): ByteArray = Inflater().run {
        var out = ByteArray(expectedSize)
        this.setInput(arr)
        val size = this.inflate(out)
       // out = out.copyOfRange(0, size - 1)
        this.end()
        return out
    }
}