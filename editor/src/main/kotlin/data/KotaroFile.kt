package data

import data.components.CameraData
import data.components.ComponentData
import data.components.TransformData
import dev.pengie.kotaro.assets.Path
import dev.pengie.kotaro.math.Quaternion
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.components.ProjectionType
import dev.pengie.kotaro.scene.components.Transform
import dev.pengie.kotaro.utils.IO
import java.io.File
import java.io.FileOutputStream

object KotaroFile {

    fun write(path: String, project: KotaroProject) {
        val bytes = byteArrayOf(
            *("KTOP".encodeToByteArray()),
            0x0,
            *("PRSE".encodeToByteArray()),
            *(project.serializeData())
        )
        val writer = FileOutputStream(File(path))
        writer.write(bytes)
        writer.close()
    }

    fun read(path: Path): KotaroProject? {
        val bytes = IO.readBytes(path)
        // Verify header
        if(!bytes.checkString(0x00, "KTOP"))
            return null
        if(!bytes.checkString(0x05, "PRSE"))
            return null
        var pointer = 0x09
        val projectName = bytes.readString(pointer).apply { pointer += length + 1}
        val productionName = bytes.readString(pointer).apply { pointer += length + 1}
        val assetDirectory = bytes.readString(pointer).apply { pointer += length + 1 }
        val scenes: MutableList<SceneData> = mutableListOf()
        while(pointer < bytes.size) {
            pointer = bytes.advanceUntil(pointer, "SCEN").apply { if (this == -1) return null }
            if(pointer == -1)
                break
            val sceneSize = bytes.readInt(pointer).apply { pointer += 4 }
            val sceneLimit = pointer + sceneSize - 1
            val sceneName = bytes.readString(pointer).apply { pointer += length + 1}
            println(pointer)
            val mainCamera = bytes.readInt(pointer).apply { pointer += 4 }
            val entities: MutableList<EntityData> = mutableListOf()
            while(pointer <= sceneLimit) {
                pointer = bytes.advanceUntil(pointer, "ENTT").apply { if (this == -1) return null }
                if(pointer == -1)
                    break
                val entitySize = bytes.readInt(pointer).apply { pointer += 4 }
                val entityLimit = pointer + entitySize - 1
                println(entityLimit)
                val entityName = bytes.readString(pointer).apply { pointer += length + 1}
                val components: MutableList<ComponentData> = mutableListOf()
                while(pointer <= entityLimit) {
                    val componentType = bytes.readString(pointer, 4).apply { pointer += 4}
                    val componentSize = bytes.readInt(pointer).apply { pointer += 4 }
                    val componentLimit = pointer + componentSize - 1
                    println(componentType)
                    val component: ComponentData? = when(componentType) {
                        "CTRM" -> {
                            val posX = bytes.readFloat(pointer).apply { pointer += 4 }
                            val posY = bytes.readFloat(pointer).apply { pointer += 4 }
                            val posZ = bytes.readFloat(pointer).apply { pointer += 4 }

                            val rotationW = bytes.readFloat(pointer).apply { pointer += 4 }
                            val rotationX = bytes.readFloat(pointer).apply { pointer += 4 }
                            val rotationY = bytes.readFloat(pointer).apply { pointer += 4 }
                            val rotationZ = bytes.readFloat(pointer).apply { pointer += 4 }

                            val scaleX = bytes.readFloat(pointer).apply { pointer += 4 }
                            val scaleY = bytes.readFloat(pointer).apply { pointer += 4 }
                            val scaleZ = bytes.readFloat(pointer).apply { pointer += 4 }

                            TransformData(Transform(
                                position = Vector3f(posX, posY, posZ),
                                rotation = Quaternion(rotationW, rotationX, rotationY, rotationZ),
                                scale = Vector3f(scaleX, scaleY, scaleZ)
                                )
                            )
                        }
                        "CCMA" -> {
                            val projectionType = if(bytes[pointer].toInt().apply { pointer++ } == 0) ProjectionType.Orthographic else ProjectionType.Perspective
                            val fov = bytes.readFloat(pointer).apply { pointer += 4 }
                            val nearPlane = bytes.readFloat(pointer).apply { pointer += 4 }
                            val farPlane = bytes.readFloat(pointer).apply { pointer += 4 }
                            val color = bytes.readColor(pointer).apply { pointer += 4 }
                            CameraData(projectionType, fov, nearPlane, farPlane, color)
                        }
                        else -> null
                    }
                    if(component != null)
                        components.add(component)
                    println(componentSize)
                    pointer = componentLimit + 1
                    println(pointer)
                }
                entities.add(EntityData(entityName, components.toTypedArray()))
                pointer = entityLimit + 1
            }
            scenes.add((SceneData(sceneName, mainCamera, arrayOf(), entities.toTypedArray())))
            pointer = sceneLimit + 1
        }
        return KotaroProject(projectName, productionName, assetDirectory, scenes.toTypedArray())
    }

}