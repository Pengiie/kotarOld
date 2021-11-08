package dev.pengie.kotaro.assets.loaders

import dev.pengie.kotaro.assets.AssetConfig
import dev.pengie.kotaro.assets.AssetLoader
import dev.pengie.kotaro.assets.AssetLoaderFactory
import dev.pengie.kotaro.assets.Path
import dev.pengie.kotaro.assets.config.FBXConfig
import dev.pengie.kotaro.data.FBXData
import dev.pengie.kotaro.graphics.Texture
import dev.pengie.kotaro.utils.FBXParser
import dev.pengie.kotaro.utils.IO

class FBXLoader: AssetLoader<FBXData> {
    private var data: FBXData? = null

    override fun loadAsync(path: Path) {
        if(path.extension != "fbx")
            throw Exception("File type ${path.extension} was given when loading FBX Data")
        val bytes = IO.readBytes(path)
        data = FBXParser.parse(bytes)
    }

    override fun loadSync(): FBXData = data!!

}

object FBXLoaderFactory : AssetLoaderFactory<FBXLoader> {
    override fun createLoader(): FBXLoader = FBXLoader()
}

@Suppress("UNCHECKED_CAST")
fun FBXLoaderFactory.cast(): AssetLoaderFactory<AssetLoader<FBXData>> = this as AssetLoaderFactory<AssetLoader<FBXData>>