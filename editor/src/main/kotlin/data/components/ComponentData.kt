package data.components

interface ComponentData {
    val componentName: String

    fun serializeData(): ByteArray
}