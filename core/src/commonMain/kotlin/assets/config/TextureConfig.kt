package dev.pengie.kotaro.assets.config

import dev.pengie.kotaro.assets.AssetConfig
import dev.pengie.kotaro.data.Interpolation
import dev.pengie.kotaro.graphics.Texture

class TextureConfig(
    val interpolation: Interpolation = Interpolation.LINEAR
): AssetConfig<Texture>