package dev.pengie.kotaro.assets

import dev.pengie.kotaro.utils.toAny
import kotlin.reflect.KClass

class Asset <T : Any> (val assetName: String, val path: Path, val type: KClass<T>, val config: AssetConfig<T>?)

@Suppress("UNCHECKED_CAST")
fun <T : Any> asset (assetName: String, path: Path, type: KClass<T>, config: AssetConfig<T>? = null): Asset<Any> =
    Asset(assetName, path, type.toAny(), config as AssetConfig<Any>?)