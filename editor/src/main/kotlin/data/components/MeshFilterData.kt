package data.components

class MeshFilterData(val mesh: String) : ComponentData {

    override val componentName: String = "CMFR"

    override fun serializeData(): ByteArray = byteArrayOf(
        *(mesh.encodeToByteArray()), 0x0
    )
}