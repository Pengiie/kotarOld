package dev.pengie.kotaro.scene

object SceneLibrary {
    private val library: HashMap<Int, SceneInstance> = hashMapOf()

    fun registerScene(scene: SceneInstance) {
        library[scene.id] = scene
    }

    operator fun get(index: Int): SceneInstance = requireNotNull(library[index]) { "Scene $index is not registered!" }
}