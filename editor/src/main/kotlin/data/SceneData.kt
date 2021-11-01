package data

import data.components.*
import dev.pengie.kotaro.assets.Asset
import dev.pengie.kotaro.assets.AssetLibrary
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.graphics.Material
import dev.pengie.kotaro.graphics.MeshFactory
import dev.pengie.kotaro.math.Quaternion
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.scene.components.light.AmbientLight
import dev.pengie.kotaro.scene.components.light.DirectionalLight
import dev.pengie.kotaro.utils.Bytes.toBytes
import scene.MeshLibrary

class SceneData(
    val name: String,
    val mainCamera: Int,
    val assets: Array<Asset<Any>>,
    val entities: Array<EntityData>
) {
    fun serializeData(): ByteArray = byteArrayOf(
        *(name.encodeToByteArray()), 0x0,
        *(mainCamera.toBytes()),
        *(assets.map { it.serializeData().toChunk("ASST") }.reduceOrNull { l, r -> l + r} ?: byteArrayOf()),
        *(entities.map {it.serializeData().toChunk("ENTT")}.reduceOrNull { l, r -> l + r} ?: byteArrayOf())
    )

    fun createScene(sceneIndex: Int, init: SceneInstance.() -> Unit): SceneInstance =
        object : SceneInstance(sceneIndex, AssetLibrary()) {
            override fun init() {
                this@SceneData.entities.forEach { data ->
                    MeshLibrary.meshes["cube"] = MeshFactory.createCube()
                    createEntity().apply {
                        addComponent(this, Tag(data.name))
                        data.components.forEach { component ->
                            when(component) {
                                is TransformData -> addComponent(this, component.transform)
                                is CameraData -> addComponent(this, Camera(
                                    component.projectionType,
                                    component.fov,
                                    component.nearPlane,
                                    component.farPlane,
                                    component.backgroundColor
                                ))
                                is MeshFilterData -> addComponent(this, MeshFilter(MeshLibrary.meshes[component.mesh]!!.copy()))
                                is MeshRendererData -> addComponent(this, MeshRenderer(Material(), component.wireframe, component.cullFaces))
                                is AmbientLightData -> addComponent(this, AmbientLight(component.color, component.strength))
                                is DirectionalLightData -> addComponent(this, DirectionalLight(component.color, component.strength))
                            }
                        }
                        mainCamera = this@SceneData.mainCamera
                    }
                }
                init.invoke(this)
            }
        }
     companion object {
         fun createDefaultScene(): SceneData =
             SceneData("Scene1", 0,
                 arrayOf(

                 ),
                 arrayOf(
                     EntityData("Main Camera", arrayOf(
                         TransformData(Transform(position = Vector3f(0f, 2f, 2f))),
                         CameraData(ProjectionType.Perspective, 90f, 0.1f, 1000f, Color(0x1597e8FF))
                     )),
                     EntityData("Cube", arrayOf(
                         TransformData(Transform()),
                         MeshFilterData("cube"),
                         MeshRendererData("default", wireframe = false, cullFaces = true)
                     )),
                     EntityData("Light", arrayOf(
                         TransformData(Transform(position = Vector3f(1f, 2f, 1f), rotation = Quaternion.euler(50f, 25f, 0f))),
                         AmbientLightData(Color.White, 0.2f),
                         DirectionalLightData(Color.White, 0.6f)
                     ))
             ))
     }
}