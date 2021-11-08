package dev.pengie.kotaro.scene

import dev.pengie.kotaro.logging.logInfo
import dev.pengie.kotaro.scene.components.Tag
import dev.pengie.kotaro.scene.components.hierarchy.Children
import dev.pengie.kotaro.scene.components.hierarchy.Parent
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
                this.indices.addAll(List(master.size) { -1 })
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

    fun setParent(parent: Entity?, child: Entity) {
        if(parent == null) {
            if(hasComponent<Parent>(child)) {
                val p = getComponent<Parent>(child)!!.parent
                getComponent<Children>(p)!!.children.remove(child)
                removeComponent<Parent>(child)
            }
        } else {
            if (!hasComponent<Children>(parent))
                addComponent(parent, Children())
            getComponent<Children>(parent)!!.children.add(child)
            if (!hasComponent<Parent>(child))
                addComponent(child, Parent(parent))
            getComponent<Parent>(child)!!.parent = parent
        }
    }

    fun createView(vararg components: KClass<*>): SceneView = SceneView(this, *components)

    override fun dispose() {
        for((type, pool) in poolMap) {
            if(type.isInstance(Disposable::class))
                pool.getAllComponents().forEach { (it as Disposable).dispose() }
        }
    }
}