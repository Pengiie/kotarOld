package data.components

import dev.pengie.kotaro.scene.components.Transform
import dev.pengie.kotaro.utils.Bytes.toBytes

class TransformData(val transform: Transform) : ComponentData {
    override val componentName: String = "CTRM"

    override fun serializeData(): ByteArray = byteArrayOf(
        *transform.position.x.toBytes(),
        *transform.position.y.toBytes(),
        *transform.position.z.toBytes(),

        *transform.rotation.w.toBytes(),
        *transform.rotation.x.toBytes(),
        *transform.rotation.y.toBytes(),
        *transform.rotation.z.toBytes(),

        *transform.scale.x.toBytes(),
        *transform.scale.y.toBytes(),
        *transform.scale.z.toBytes(),
    )
}