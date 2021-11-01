package data.components

import data.serializeData
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.scene.components.ProjectionType
import dev.pengie.kotaro.utils.Bytes.toBytes

class CameraData(
    val projectionType: ProjectionType,
    val fov: Float,
    val nearPlane: Float,
    val farPlane: Float,
    val backgroundColor: Color
) : ComponentData {
    override val componentName: String = "CCMA"

    override fun serializeData(): ByteArray = byteArrayOf(
        (if(projectionType == ProjectionType.Orthographic) 0 else 1).toByte(),
        *(fov.toBytes()),
        *(nearPlane.toBytes()),
        *(farPlane.toBytes()),
        *(backgroundColor.serializeData())
    )
}