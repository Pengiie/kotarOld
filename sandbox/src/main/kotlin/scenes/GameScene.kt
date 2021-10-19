package scenes

import dev.pengie.kotaro.Time
import dev.pengie.kotaro.assets.AssetLibrary
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.input.KeyDownEvent
import dev.pengie.kotaro.graphics.MeshFactory
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.input.Key
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.script.EntityBehaviour

object GameScene : SceneInstance(0, AssetLibrary()) {
    override fun init() {
        Input.mouseLocked = true
        EventManager.registerListener(EventListener.create<KeyDownEvent> {
            if(it.key == Key.ESCAPE)
                Input.mouseLocked = !Input.mouseLocked
        })

        createEntity().apply {
            mainCamera = this
            addComponent(this, Transform())
            addComponent(this, Camera())
            addComponent(this, Script(object : EntityBehaviour() {
                private lateinit var transform: Transform
                private val speed = 1f

                override fun start() {
                    transform = getComponent<Transform>()!!
                }

                override fun update() {
                    if(Input.mouseLocked) {
                        transform.rotation.z -= Input.mousePosition.y
                        transform.rotation.y -= Input.mousePosition.x

                        val horizontal = Input.getHorizontalAxis() * speed * Time.deltaTime
                        val vertical = Input.getVerticalAxis() * speed * Time.deltaTime
                        transform.position += transform.forward * vertical + transform.right * horizontal

                        var rise = 0f
                        if(Input.isKeyDown(Key.SPACE))
                            rise += speed
                        if(Input.isKeyDown(Key.LEFT_SHIFT))
                            rise -= speed
                        rise *= Time.deltaTime

                        transform.position.y += rise
                    }
                }
            }))
        }
        createEntity().apply {
            addComponent(this, Transform(position = Vector3f(0f, 0f, -1f)))
            addComponent(this, MeshFilter(MeshFactory.createCube()))
            addComponent(this, MeshRenderer(wireframe = true))
        }
    }
}