package dev.pengie.kotaro.assets

import kotlin.reflect.KClass

object AssetManager {
    private val loaders: HashMap<KClass<Any>, AssetLoader> = hashMapOf(

    )
    private val loadingQueue: ArrayDeque<String> = ArrayDeque()



    internal class AssetDescriptor(val name: String, val filePath: Path, val type: KClass<Any>)
}