package dev.pengie.kotaro.graphics

private var globalId: Int = 0

class Mesh(
    val vertices: MutableList<Float>,
    val textureCoords: MutableList<Float> = mutableListOf(),
    val colors: MutableList<Float> = mutableListOf(),
    val normals: MutableList<Float> = mutableListOf(),
    val indices: MutableList<Int>) {
    internal val meshId: Int = globalId++
}