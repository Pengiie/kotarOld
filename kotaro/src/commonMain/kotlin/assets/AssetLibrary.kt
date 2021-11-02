package dev.pengie.kotaro.assets

import dev.pengie.kotaro.types.Disposable
import kotlin.reflect.KClass

class AssetLibrary(private vararg val assets: Asset<Any>) : Disposable {
    private val loadedAssets: HashMap<String, Any> = hashMapOf()

    internal fun load() {
        assets.forEach {
            AssetManager.loadAsset(it) { asset ->
                loadedAssets[it.assetName] = asset
            }
        }
        AssetManager.finishLoading()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getAsset(name: String): T {
        return checkNotNull(this.loadedAssets[name]) { "Asset \"$name\" does not exist" } as T
    }

    override fun dispose() {
        loadedAssets.values.forEach {
            if(it is Disposable)
                it.dispose()
        }
    }
}