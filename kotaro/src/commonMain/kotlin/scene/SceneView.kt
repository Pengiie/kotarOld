package dev.pengie.kotaro.scene

import kotlin.reflect.KClass

class SceneView(scene: Scene, vararg componentTypes: KClass<*>) : Iterable<Entity> {
    private val entities: MutableList<Entity> = mutableListOf()

    init {
        run {
            var matchingPools: List<ComponentPool<*>> = componentTypes.mapNotNull { scene.poolMap[it] }
            if(matchingPools.size != componentTypes.size)
                return@run
            val smallest = matchingPools.minByOrNull { it.entities.size }
            matchingPools = matchingPools.filter { it != smallest }
            smallest?.entities?.forEach { entity ->
                if(matchingPools.all { it.hasComponent(entity)}) entities.add(entity)
            }
        }
    }

    override fun iterator(): Iterator<Entity> = entities.iterator()
}