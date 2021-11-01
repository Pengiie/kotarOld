package dev.pengie.kotaro.scene

import dev.pengie.kotaro.types.Disposable
import kotlin.reflect.KClass

open class Scene : Disposable {
    val entities: MutableList<Entity> = mutableListOf()
    val poolMap: MutableMap<KClass<*>, ComponentPool<*>> = mutableMapOf()

    fun createEntity(builder: EntityBuilder.() -> Unit = {}): Entity = entities.size.apply {
        entities.add(entities.size)
        poolMap.values.forEach { it.indices.add(-1) }
        builder.invoke(EntityBuilder(this@Scene, this))
    }

    fun removeEntity(entity: Entity) {
        poolMap.values.forEach { if(it.hasComponent(entity)) it.removeComponent(entity) }
    }

    inline fun <reified T> getComponentPool(): ComponentPool<T> =
        if(poolMap.containsKey(T::class) )
            @Suppress("UNCHECKED_CAST")
            poolMap[T::class]!! as ComponentPool<T>
        else {
            val master = entities
            ComponentPool<T>().apply {
                poolMap[T::class] = this
                this.indices.addAll(List(master.size) { 0 })
            }
        }

    inline fun <reified T> addComponent(entity: Entity, component: T) {
        getComponentPool<T>().addComponent(entity, component)
    }

    inline fun <reified T> hasComponent(entity: Entity): Boolean = getComponentPool<T>().hasComponent(entity)

    inline fun <reified T> getComponent(entity: Entity): T? =
        if(hasComponent<T>(entity))
            getComponentPool<T>().getComponent(entity)
        else
            null


    inline fun <reified T> removeComponent(entity: Entity) {
        getComponentPool<T>().removeComponent(entity)
    }

    fun createView(vararg components: KClass<*>): SceneView = SceneView(this, *components)

    override fun dispose() {
        for((type, pool) in poolMap) {
            if(type.isInstance(Disposable::class))
                pool.getAllComponents().forEach { (it as Disposable).dispose() }
        }
    }
}