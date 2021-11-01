package data.components

class MeshRendererData(val material: String, val wireframe: Boolean, val cullFaces: Boolean) : ComponentData {
    override val componentName: String = "CMRR"
    override fun serializeData(): ByteArray = byteArrayOf(
        *(material.encodeToByteArray()), 0x0,
        (if(wireframe) 0x1 else 0x0),
        (if(cullFaces) 0x1 else 0x0),
    )
}