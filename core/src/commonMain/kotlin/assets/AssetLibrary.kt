package dev.pengie.kotaro.assets

import kotlin.reflect.KClass

class AssetLibrary(vararg assets: Pair<String, KClass<Any>>) {
    private val assets: HashMap<String, Any> = hashMapOf()

    fun load(async: Boolean) {

    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getAsset(name: String): T {
        return checkNotNull(this.assets[name]) { "Asset \"$name\" does not exist" } as T
    }
}