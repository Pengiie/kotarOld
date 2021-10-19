package dev.pengie.kotaro.graphics

object MeshFactory {
    fun createPlane(): Mesh = Mesh(
        vertices = mutableListOf(
            0f, 0f, 0f,
            1f, 0f, 0f,
            0f, 1f, 0f,
            1f, 1f, 0f
        ),
        textureCoords = mutableListOf(
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
        ),
        normals = mutableListOf(
          0f, 0f, 1f,
          0f, 0f, 1f,
          0f, 0f, 1f,
          0f, 0f, 1f,
        ),
        indices = mutableListOf(
            0, 1, 2,
            2, 1, 3
        )
    )

    fun createCube(): Mesh = Mesh(
        vertices = mutableListOf(
            // Bottom
            0f, 0f, 0f,
            1f, 0f, 0f,
            0f, 0f, 1f,
            1f, 0f, 1f,

            // Top
            0f, 1f, 0f,
            1f, 1f, 0f,
            0f, 1f, 1f,
            1f, 1f, 1f,

            // Left
            1f, 0f, 0f,
            1f, 1f, 0f,
            1f, 0f, 1f,
            1f, 1f, 1f,

            // Right
            0f, 0f, 0f,
            0f, 1f, 0f,
            0f, 0f, 1f,
            0f, 1f, 1f,

            // Front
            0f, 0f, 0f,
            1f, 0f, 0f,
            0f, 1f, 0f,
            1f, 1f, 0f,

            // Back
            0f, 0f, 1f,
            1f, 0f, 1f,
            0f, 1f, 1f,
            1f, 1f, 1f,
        ),
        textureCoords = mutableListOf(
            // Bottom
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            // Top
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            // Left
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            // Right
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            // Front
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,

            // Back
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f,
        ),
        indices = mutableListOf(
            // Bottom
            1, 2, 0,
            3, 2, 1,

            // Top
            6, 5, 4,
            7, 5, 6,

            // Left
            9, 10, 8,
            11, 10, 9,

            // Right
            12, 14, 13,
            13, 14, 15,

            // Front
            16, 18, 17,
            17, 18, 19,

            // Back
            21, 22, 20,
            23, 22, 21,
        )
    )
}