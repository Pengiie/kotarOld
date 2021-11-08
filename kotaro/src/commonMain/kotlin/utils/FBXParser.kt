package dev.pengie.kotaro.utils

import dev.pengie.kotaro.data.FBXData
import dev.pengie.kotaro.graphics.Mesh
import dev.pengie.kotaro.utils.Bytes.checkString

object FBXParser {

    fun parse(bytes: ByteArray): FBXData {
        val correctHeader = bytes.checkString(0, "Kaydara FBX Binary  ")
        if(!correctHeader)
            throw Exception("Received malformed FBX header data")

        return FBXData(Mesh(vertices = mutableListOf(), indices = mutableListOf()))
    }
}