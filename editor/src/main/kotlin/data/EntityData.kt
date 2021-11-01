package data

import data.components.ComponentData

class EntityData(
    val name: String,
    val components: Array<ComponentData>
) {
    fun serializeData(): ByteArray = byteArrayOf(
        *(name.encodeToByteArray()), 0x0,
        *(components.map {it.serializeData().toChunk(it.componentName)}.reduceOrNull { l, r -> l + r}  ?: byteArrayOf())
    )
}