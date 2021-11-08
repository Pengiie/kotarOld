package dev.pengie.kotaro.assets.loaders

import dev.pengie.kotaro.assets.AssetLoader
import dev.pengie.kotaro.assets.AssetLoaderFactory
import dev.pengie.kotaro.assets.Path
import dev.pengie.kotaro.graphics.Mesh
import dev.pengie.kotaro.graphics.TexData
import dev.pengie.kotaro.graphics.Texture
import dev.pengie.kotaro.graphics.TextureFactory
import dev.pengie.kotaro.utils.IO
import dev.pengie.kotaro.utils.ImageParser
import dev.pengie.kotaro.utils.MeshParser

class MeshLoader : AssetLoader<Mesh> {
    private var mesh: Mesh? = null

    override fun loadAsync(path: Path) {
        val bytes = IO.readBytes(path)
        mesh = MeshParser.parse(bytes)
    }

    override fun loadSync(): Mesh {
        return mesh!!
    }
}

object MeshLoaderFactory : AssetLoaderFactory<MeshLoader> {
    override fun createLoader(): MeshLoader = MeshLoader()
}

@Suppress("UNCHECKED_CAST")
fun MeshLoaderFactory.cast(): AssetLoaderFactory<AssetLoader<Mesh>> = this as AssetLoaderFactory<AssetLoader<Mesh>>