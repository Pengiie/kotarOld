package dev.pengie.kotaro.assets

import dev.pengie.kotaro.utils.toAny
import kotlin.reflect.KClass

class Asset <T : Any> (val assetName: String, val path: Path, val type: KClass<T>)

@Suppress("UNCHECKED_CAST")
fun <T : Any> asset (assetName: String, path: String, type: KClass<T>): Asset<Any> =
    Asset(assetName, Path(path), type.toAny())