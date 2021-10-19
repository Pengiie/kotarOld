package scene

import EDITOR_SCENE
import dev.pengie.kotaro.assets.AssetLibrary
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.graphics.MeshFactory
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*

object EditorScene : SceneInstance(EDITOR_SCENE, AssetLibrary()) {

    override fun init() {
        this.mainCamera = createEntity().apply {
            addComponent(this, Transform(position = Vector3f(-1f, 2f, 2f), rotation = Vector3f(0f, -20f, -45f)))
            addComponent(this, Camera(backgroundColor = Color(0x6E7075FF)))
            addComponent(this, Script(EditorCamera()))
        }
        createEntity().apply {
            addComponent(this, Transform(position = Vector3f(0f, 0f, -3f)))
            addComponent(this, MeshFilter(MeshFactory.createCube()))
            addComponent(this, MeshRenderer())
        }
    }
}