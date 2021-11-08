package dev.pengie.kotaro.scene

import dev.pengie.kotaro.math.Quaternion
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.components.Tag
import dev.pengie.kotaro.scene.components.Transform

typealias Entity = Int

class EntityBuilder(val scene: Scene, val id: Int) {
    fun transform(position: Vector3f = Vector3f(0f), rotation: Quaternion = Quaternion(), scale: Vector3f = Vector3f(1f)): Transform =
        Transform(scene, id, position, rotation, scale).apply {
            addComponent(this)
        }

    fun name(name: String): Tag =
        Tag(name).apply {
            addComponent(this)
        }

    fun child(builder: EntityBuilder.() -> Unit): Entity =
        scene.createEntity(builder).apply {
            scene.setParent(id, this)
        }

    fun setParent(parent: Entity) {
        scene.setParent(parent, id)
    }

    inline fun <reified T> addComponent(component: T) =
        scene.addComponent(id,component)
}