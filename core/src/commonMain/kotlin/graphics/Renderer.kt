package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.scene.SceneInstance

interface Renderer {

    fun init()

    fun render(scene: SceneInstance)

}