package dev.pengie.kotaro.script

import dev.pengie.kotaro.scene.Entity
import dev.pengie.kotaro.scene.Scene
import dev.pengie.kotaro.scene.SceneInstance

open class EntityBehaviour {
    var scene: Scene? = null
        internal set
    var entity: Entity? = null
        internal set

    /**
     * Gets called the first frame the entity exists.
     */
    open fun start() {}

    /**
     * Gets called every frame.
     */
    open fun update() {}

    /**
     * Adds a component to this entity, must be called in one of the supplied methods.
     * @param component the component to add.
     */
    inline fun <reified T> addComponent(component: T) {
        entity?.let { scene?.addComponent(it, component) }
    }

    inline fun <reified T> removeComponent() {
        entity?.let { scene?.removeComponent<T>(it) }
    }

    inline fun <reified T> getComponent(): T? =
        entity?.let { scene?.getComponent<T>(it) }
}