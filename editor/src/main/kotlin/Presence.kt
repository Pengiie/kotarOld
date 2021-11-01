import data.KotaroProject
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.graphics.Material
import dev.pengie.kotaro.graphics.MeshFactory
import dev.pengie.kotaro.math.Quaternion
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.SceneLibrary
import dev.pengie.kotaro.scene.components.*
import scene.EditorCamera
import scene.EditorOnly

class Presence(val project: KotaroProject) {

    var currentScene: Int? = null

    companion object {
        var presence: Presence? = null
            private set
        fun createPresence(project: KotaroProject) {
            presence = Presence(project)
            if(project.scenes.isNotEmpty()) {
                SceneLibrary.registerScene(project.scenes[0].createScene(EDITOR_SCENE) {
                    entities.forEach { println(it) }
                    val texture = getComponent<Camera>(project.scenes[0].mainCamera)!!.renderLayer.getTexture()
                    createEntity().apply {
                        addComponent(this, EditorOnly())
                        addComponent(this, Transform(position = Vector3f(0f, 3f, 0f)))
                        addComponent(this, MeshFilter(MeshFactory.createPlane()))
                        addComponent(this, MeshRenderer(Material(texture = texture, lighting = false), cullFaces = false))
                    }
                    mainCamera = createEntity().apply {
                        addComponent(this, EditorOnly())
                        addComponent(
                            this,
                            Transform(position = Vector3f(-1f, 2f, 2f), rotation = Quaternion.euler(0f, -20f, 0f))
                        )
                        addComponent(this, Camera(backgroundColor = Color(0x6E7075FF)))
                        addComponent(this, Script(EditorCamera()))
                    }
                })
                presence!!.currentScene = 0
            }
        }
        fun hasPresence(): Boolean = presence != null
    }
}