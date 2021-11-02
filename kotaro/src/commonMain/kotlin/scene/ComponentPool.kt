package dev.pengie.kotaro.scene

import dev.pengie.kotaro.types.Disposable

class ComponentPool<T> {
    val indices: MutableList<Int> = mutableListOf()
    internal val entities: MutableList<Entity> = mutableListOf()
    internal val components: MutableList<T> = mutableListOf()

    fun hasComponent(entity: Entity): Boolean = indices[entity] != -1

    fun addComponent(entity: Entity, component: T) {
        indices[entity] = entities.size
        entities.add(entity)
        components.add(component)
    }

    fun getComponent(entity: Entity): T = components[indices[entity]]

    fun removeComponent(entity: Entity) {
        entities.removeAt(indices[entity])
        // If the component is disposable, dispose it
        (getComponent(entity) as? Disposable)?.dispose()
        components.removeAt(indices[entity])
        indices[entity] = -1
    }

    fun getAllComponents(): List<T> = entities.map { getComponent(it) }
}