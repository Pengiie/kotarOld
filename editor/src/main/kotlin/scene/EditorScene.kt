package scene

import EDITOR_SCENE
import dev.pengie.kotaro.assets.AssetLibrary
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.graphics.Material
import dev.pengie.kotaro.graphics.MeshFactory
import dev.pengie.kotaro.math.Quaternion
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.scene.components.light.AmbientLight
import dev.pengie.kotaro.scene.components.light.DirectionalLight

object EditorScene : SceneInstance(EDITOR_SCENE, AssetLibrary()) {

    override fun init() {
        this.mainCamera = createEntity().apply {
            addComponent(this, Transform(position = Vector3f(-1f, 2f, 2f), rotation = Quaternion.euler(0f, -20f, 0f)))
            addComponent(this, Camera(backgroundColor = Color(0x6E7075FF)))
            addComponent(this, Script(EditorCamera()))
        }
        createEntity().apply {
            addComponent(this, Transform(position = Vector3f(0f, 0f, -3f)))
            addComponent(this, MeshFilter(MeshFactory.createCube()))
            addComponent(this, MeshRenderer())
        }
        createEntity().apply {
            addComponent(this, AmbientLight(strength = 0.2f))
        }
        createEntity().apply {
            addComponent(this, Transform(rotation = Quaternion.euler(50f, 25f, 0f), scale = Vector3f(0.25f, 0.25f, 0.5f)))
            addComponent(this, DirectionalLight())
            addComponent(this, MeshFilter(MeshFactory.createCube()))
            addComponent(this, MeshRenderer(material = Material(lighting = false), wireframe = true, cullFaces = false))
        }
    }
}