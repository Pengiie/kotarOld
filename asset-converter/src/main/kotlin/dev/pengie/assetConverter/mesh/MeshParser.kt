package dev.pengie.assetConverter.mesh

import dev.pengie.assetConverter.Bytes
import dev.pengie.assetConverter.Bytes.toBytes
import dev.pengie.assetConverter.Path
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader

object MeshParser {

    fun parseOBJ(bytes: ByteArray, path: Path): ByteArray {
        val vertices: MutableList<Float> = mutableListOf()
        val textureCoords: MutableList<Float> = mutableListOf()
        val normals: MutableList<Float> = mutableListOf()
        val indices: MutableList<Int> = mutableListOf()

        val reader = BufferedReader(InputStreamReader(ByteArrayInputStream(bytes)))

        val verts: MutableList<Vec3> = mutableListOf()
        val texs: MutableList<Vec2> = mutableListOf()
        val norms: MutableList<Vec3> = mutableListOf()

        var i = 0

        var line: String?
        var lineNumber = 0
        while(true) {
            println(lineNumber++)
            line = reader.readLine()
            if(line == null)
                break
            if(line.startsWith("v ")) {
                val components = line.substring(2).split(" ")
                verts.add(Vec3(components[0].toFloat(), components[1].toFloat(), components[2].toFloat()))
            } else if(line.startsWith("vt ")) {
                val components = line.substring(3).split(" ")
                texs.add(Vec2(components[0].toFloat(), components[1].toFloat()))
            } else if(line.startsWith("vn ")) {
                val components = line.substring(3).split(" ")
                norms.add(Vec3(components[0].toFloat(), components[1].toFloat(), components[2].toFloat()))
            } else if(line.startsWith("f ")) {
                val components = line.substring(2).split(" ")
                components.forEach {
                    val vertex = it.split("/")
                    val pos = verts[vertex[0].toInt() - 1]
                    vertices.addAll(listOf(pos.x, pos.y, pos.z))
                    if(vertex[1].isEmpty())
                        textureCoords.addAll(listOf(0f, 0f))
                    else {
                        val tex = texs[vertex[1].toInt() - 1]
                        textureCoords.addAll(listOf(tex.x, tex.y))
                    }
                    val norm = norms[vertex[2].toInt() - 1]
                    normals.addAll(listOf(norm.x, norm.y, norm.z))
                }
                println("size: ${components.size}")
                if(components.size == 3)
                    indices.addAll(listOf(
                        i, i + 1, i + 2
                    ))
                else if(components.size == 4)
                    indices.addAll(listOf(
                        i,     i + 1, i + 2,
                        i    , i + 2, i + 3))
                println("i :${indices.size}")
                i += components.size
            }
        }
        return constructMeshAsset(Mesh(vertices, textureCoords, normals, indices), path)
    }

    class Vec3(val x: Float, val y: Float, val z: Float)
    class Vec2(val x: Float, val y: Float)

    /**
     * Mesh Type - 1
     * 4 bytes - vertices length
     * n bytes - vertices
     * n bytes - texture coords
     * n bytes - normals
     * 4 bytes - indices length
     * n bytes - indices
     */
    private fun constructMeshAsset(mesh: Mesh, path: Path): ByteArray {
        return byteArrayOf(
            *Bytes.generateHeader(1, path),
            *(mesh.vertices.size / 3).toBytes(),
            *((mesh.vertices.map { it.toBytes() }).reduceOrNull { l, r -> byteArrayOf(*l, *r)} ?: byteArrayOf()),
            *((mesh.textureCoords.map { it.toBytes() }).reduceOrNull { l, r -> byteArrayOf(*l, *r)} ?: byteArrayOf()),
            *((mesh.normals.map { it.toBytes() }).reduceOrNull { l, r -> byteArrayOf(*l, *r)} ?: byteArrayOf()),
            *mesh.indices.size.toBytes(),
            *((mesh.indices.map { it.toBytes() }).reduceOrNull { l, r -> byteArrayOf(*l, *r)} ?: byteArrayOf())
        )
    }

}