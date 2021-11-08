package dev.pengie.assetConverter.mesh

class Mesh(
    val vertices: MutableList<Float>,
    val textureCoords: MutableList<Float>,
    val normals: MutableList<Float>,
    val indices: MutableList<Int>,
)