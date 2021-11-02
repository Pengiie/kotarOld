package dev.pengie.kotaro.assets.loaders

import dev.pengie.kotaro.assets.AssetConfig
import dev.pengie.kotaro.assets.AssetLoader
import dev.pengie.kotaro.assets.AssetLoaderFactory
import dev.pengie.kotaro.assets.Path
import dev.pengie.kotaro.assets.config.TextureConfig
import dev.pengie.kotaro.graphics.TexData
import dev.pengie.kotaro.graphics.Texture
import dev.pengie.kotaro.graphics.TextureFactory
import dev.pengie.kotaro.utils.IO
import dev.pengie.kotaro.utils.ImageParser

class TextureLoader : AssetLoader<Texture, TextureConfig> {
    private var texData: TexData? = null

    override fun loadAsync(path: Path, config: TextureConfig?) {
        val bytes = IO.readBytes(path)
        texData = when(path.extension) {
            "png" -> ImageParser.parsePNG(bytes)
            else -> throw Exception("File type ${path.extension} not supported for texture loading")
        }
        texData = ImageParser.parsePNG(bytes)
        texData!!.interpolation = (config ?: TextureConfig()).interpolation
    }

    override fun loadSync(): Texture {
        return TextureFactory().apply { init(texData!!) }
    }
}

object TextureLoaderFactory : AssetLoaderFactory<TextureLoader> {
    override fun createLoader(): TextureLoader = TextureLoader()
}

@Suppress("UNCHECKED_CAST")
fun TextureLoaderFactory.cast(): AssetLoaderFactory<AssetLoader<Texture, AssetConfig<Texture>>> = this as AssetLoaderFactory<AssetLoader<Texture, AssetConfig<Texture>>>