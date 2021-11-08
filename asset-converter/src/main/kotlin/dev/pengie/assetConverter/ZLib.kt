package dev.pengie.assetConverter

import java.util.zip.Deflater
import java.util.zip.Inflater

object ZLib {
    fun compress(arr: ByteArray): ByteArray = Deflater().run {
        val out = ByteArray(1024)
        val bytes: MutableList<Byte> = mutableListOf()
        this.setInput(arr)
        this.finish()
        while(!this.finished()) {
            this.deflate(out)
            bytes.addAll(out.toList())
        }
        this.end()
        return bytes.toByteArray()
    }
    fun decompress(arr: ByteArray, expectedSize: Int): ByteArray = Inflater().run {
        val out = ByteArray(expectedSize)
        this.setInput(arr)
        this.inflate(out)
       // out = out.copyOfRange(0, size - 1)
        this.end()
        return out
    }
}