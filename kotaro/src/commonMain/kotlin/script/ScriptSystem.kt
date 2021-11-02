package dev.pengie.kotaro.script

import dev.pengie.kotaro.scene.Entity
import dev.pengie.kotaro.scene.Scene
import dev.pengie.kotaro.scene.components.Script

internal object ScriptSystem {
    fun update(scene: Scene) {
        for(entity in scene.createView(Script::class)) {
            val scripts = scene.getComponent<Script>(entity)!!.scripts
            scripts.forEach {
                ScriptLibrary.start(entity, it, scene)
                it.update()
            }
        }
    }

    private object ScriptLibrary {
        private val started: HashSet<Entity> = hashSetOf()

        /**
         * Calls an entity's script's start method if not yet called.
         * @param entity the entity with the script.
         * @param script the script of the associated entity.
         * @param scene the scene the entity resides in.
         * @return if the start method was called.
         */
        fun start(entity: Entity, script: EntityBehaviour, scene: Scene): Boolean {
            return if (started.contains(entity))
                true
            else {
                started.add(entity)
                script.scene = scene
                script.entity = entity
                script.start()
                false
            }
        }
    }
}