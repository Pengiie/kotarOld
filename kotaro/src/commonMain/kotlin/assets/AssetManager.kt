package dev.pengie.kotaro.assets

import dev.pengie.kotaro.assets.loaders.FBXLoaderFactory
import dev.pengie.kotaro.assets.loaders.MeshLoaderFactory
import dev.pengie.kotaro.assets.loaders.TextureLoaderFactory
import dev.pengie.kotaro.assets.loaders.cast
import dev.pengie.kotaro.data.FBXData
import dev.pengie.kotaro.graphics.Mesh
import dev.pengie.kotaro.graphics.Texture
import dev.pengie.kotaro.types.Disposable
import dev.pengie.kotaro.utils.toAny
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

object AssetManager : Disposable {
    private val asyncScope = CoroutineScope(EmptyCoroutineContext)

    private val loaders: HashMap<KClass<Any>, AssetLoaderFactory<Any>> = hashMapOf(
        makeLoader(Texture::class, TextureLoaderFactory.cast()),
        makeLoader(Mesh::class, MeshLoaderFactory.cast())
    )
    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T: Any, R: AssetLoader<T>> makeLoader(clazz: KClass<T>, loader: AssetLoaderFactory<R>): Pair<KClass<Any>, AssetLoaderFactory<Any>> = Pair(clazz.toAny(), loader as AssetLoaderFactory<Any>)

    private val tasks: ArrayDeque<LoadingTask<Any>> = ArrayDeque()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> loadAsset(asset: Asset<T>, loadCallback: (Any) -> Unit) {
        val loader = checkNotNull(loaders[asset.type as KClass<Any>]) {
            "Could not find a loader for asset ${asset.assetName}"
        }
        tasks.add(LoadingTask(AssetDescriptor(asset.assetName, Path("${asset.path.fullPath}.ktasset"), asset.type),
            loader.createLoader() as AssetLoader<Any>, asyncScope, loadCallback))
    }

    fun update() {
        if(isFinished())
            return
        val task = tasks.first()

        if(task.update()) {
            tasks.removeFirst()
            task.callback.invoke(task.loadedAsset!!)
        }
    }

    fun finishLoading() {
        while(!isFinished())
            update()
    }

    fun isFinished() = tasks.isEmpty()

    class AssetDescriptor <T: Any> (val name: String, val filePath: Path, val type: KClass<T>)

    override fun dispose() {
        asyncScope.cancel()
    }


}