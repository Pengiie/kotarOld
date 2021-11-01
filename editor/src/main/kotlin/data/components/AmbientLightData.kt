package data.components

import data.serializeData
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.utils.Bytes.toBytes

class AmbientLightData(val color: Color, val strength: Float) : ComponentData {
    override val componentName: String = "CLAM"

    override fun serializeData(): ByteArray = byteArrayOf(
        *(color.serializeData()),
        *(strength.toBytes())
    )
}