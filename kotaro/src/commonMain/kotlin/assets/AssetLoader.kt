package dev.pengie.kotaro.assets

interface AssetLoader<T : Any> {
    fun loadAsync(path: Path)
    fun loadSync(): T
}

interface AssetLoaderFactory<T> {
    fun createLoader(): T
}