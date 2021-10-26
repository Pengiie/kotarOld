package dev.pengie.kotaro.assets

interface AssetLoader<T : Any, R: AssetConfig<T>> {
    fun loadAsync(path: Path, config: R?)
    fun loadSync(): T
}

interface AssetLoaderFactory<T> {
    fun createLoader(): T
}