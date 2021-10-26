package dev.pengie.kotaro.assets

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoadingTask <T: Any>(
    val asset: AssetManager.AssetDescriptor<Any>,
    val loader: AssetLoader<T, AssetConfig<T>>,
    val scope: CoroutineScope,
    val callback: (T) -> Unit
) {
    var job: Job? = null
    var loadedAsset: T? = null
        private set

    @Suppress("UNCHECKED_CAST")
    fun update(): Boolean {
        if(job == null) {
            job = scope.launch {
                loader.loadAsync(asset.filePath, asset.config as AssetConfig<T>?)
            }
        } else if(job!!.isCompleted) {
            loadedAsset = loader.loadSync()
            return true
        }
        return false
    }
}