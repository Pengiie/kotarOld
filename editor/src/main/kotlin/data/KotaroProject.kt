package data

class KotaroProject(
    val name: String,
    val productionName: String,
    val assetDirectory: String,
    val scenes: Array<SceneData>) {

    fun serializeData(): ByteArray = byteArrayOf(
        *(name.encodeToByteArray()), 0x00,
        *(productionName.encodeToByteArray()), 0x00,
        *(assetDirectory.encodeToByteArray()), 0x00,
        *(scenes.map {it.serializeData().toChunk("SCEN")}.reduceOrNull { l, r -> l + r} ?: byteArrayOf())
    )
}