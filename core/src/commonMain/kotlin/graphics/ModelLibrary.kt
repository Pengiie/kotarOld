package dev.pengie.kotaro.graphics

internal object ModelLibrary {
    private val models: HashMap<Int, Model> = hashMapOf()

    fun getModel(mesh: Mesh) = models[mesh.meshId] ?: ModelFactory.createModel().apply {
        this.create(mesh)
        models[mesh.meshId] = this
    }
}