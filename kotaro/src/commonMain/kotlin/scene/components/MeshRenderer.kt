package dev.pengie.kotaro.scene.components

import dev.pengie.kotaro.graphics.DrawLayer
import dev.pengie.kotaro.graphics.Material
import dev.pengie.kotaro.graphics.Model

class MeshRenderer(
    val material: Material = Material(),
    val shader: String? = null,
    val wireframe: Boolean = false,
    val cullFaces: Boolean = true,
    val layer: DrawLayer = DrawLayer.ENTITY
)