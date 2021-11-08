package dev.pengie.kotaro.utils

import dev.pengie.kotaro.graphics.Mesh
import dev.pengie.kotaro.utils.Bytes.checkString

object MeshParser {

    fun parse(bytes: ByteArray): Mesh {
        val vertices: MutableList<Float> = mutableListOf()
        val textureCoords: MutableList<Float> = mutableListOf()
        val normals: MutableList<Float> = mutableListOf()
        val indices: MutableList<Int> = mutableListOf()

        if(!bytes.checkString(0, "KTASSET"))
            throw Exception("Incorrect header data in asset file")
        if(bytes[0x8].toInt() != 1)
            throw Exception("Wrong asset typing for mesh")

        var pointer = 0x11
        val verticesLength = Bytes.toInt(bytes[pointer], bytes[++pointer], bytes[++pointer], bytes[++pointer])
        pointer++
        (pointer..(pointer + verticesLength * 12) step 4).forEach {
            vertices.add(FloatBytes.toFloat(bytes[it], bytes[it + 1], bytes[it + 2], bytes[it + 3]))
        }
        pointer += verticesLength * 12
        (pointer until (pointer + verticesLength * 8) step 4).forEach {
            textureCoords.add(FloatBytes.toFloat(bytes[it], bytes[it + 1], bytes[it + 2], bytes[it + 3]))
        }
        pointer += verticesLength * 8
        (pointer until (pointer + verticesLength * 12) step 4).forEach {
            normals.add(FloatBytes.toFloat(bytes[it], bytes[it + 1], bytes[it + 2], bytes[it + 3]))
        }
        pointer += verticesLength * 12
        val indicesLength = Bytes.toInt(bytes[pointer], bytes[pointer + 1], bytes[pointer + 2], bytes[pointer + 3])
        pointer += 4
        (pointer until (pointer + indicesLength * 4) step 4).forEach {
            indices.add(Bytes.toInt(bytes[it], bytes[it + 1], bytes[it + 2], bytes[it + 3]))
        }
        return Mesh(vertices = vertices, textureCoords = textureCoords, normals = normals, indices = indices)
    }
}