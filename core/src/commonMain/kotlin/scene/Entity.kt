package dev.pengie.kotaro.scene

typealias Entity = Int

class EntityBuilder(val scene: Scene, val id: Int) {
    inline fun <reified T> addComponent(component: T) =
        scene.addComponent(id,component)
}