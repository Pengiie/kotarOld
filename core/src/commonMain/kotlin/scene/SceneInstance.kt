package dev.pengie.kotaro.scene

import dev.pengie.kotaro.assets.AssetLibrary
import dev.pengie.kotaro.scene.components.Camera

abstract class SceneInstance(internal val id: Int, val assets: AssetLibrary) : Scene() {
    var mainCamera: Entity? = null
    abstract fun init()
}