package data.components

import data.serializeData
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.utils.Bytes.toBytes

class DirectionalLightData(val color: Color, val strength: Float) : ComponentData {
    override val componentName: String = "CLDR"

    override fun serializeData(): ByteArray = byteArrayOf(
        *(color.serializeData()),
        *(strength.toBytes())
    )
}