package scenes

import dev.pengie.kotaro.Time
import dev.pengie.kotaro.assets.Asset
import dev.pengie.kotaro.assets.AssetLibrary
import dev.pengie.kotaro.assets.Path
import dev.pengie.kotaro.assets.asset
import dev.pengie.kotaro.assets.config.TextureConfig
import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.data.Interpolation
import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.input.KeyDownEvent
import dev.pengie.kotaro.graphics.Material
import dev.pengie.kotaro.graphics.MeshFactory
import dev.pengie.kotaro.graphics.Texture
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.input.Key
import dev.pengie.kotaro.logging.logInfo
import dev.pengie.kotaro.math.*
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.components.*
import dev.pengie.kotaro.scene.components.light.AmbientLight
import dev.pengie.kotaro.scene.components.light.PointLight
import dev.pengie.kotaro.script.EntityBehaviour
import dev.pengie.kotaro.utils.toAny

object GameScene : SceneInstance(0, AssetLibrary(
    asset("dirt", Path("dirt.png"), Texture::class, TextureConfig(Interpolation.NEAREST)),
    asset("sun", Path("sun.png"), Texture::class, TextureConfig(Interpolation.LINEAR))
)) {
    override fun init() {
        logInfo("Starting main scene")
        Input.mouseLocked = true
        EventManager.registerListener(EventListener.create<KeyDownEvent> {
            if(it.key == Key.ESCAPE)
                Input.mouseLocked = !Input.mouseLocked
        })
        var cameraTransform: Transform
        createEntity().apply {
            mainCamera = this
            cameraTransform = Transform(this@GameScene, this)
            addComponent(this, cameraTransform)
            addComponent(this, Camera())
            addComponent(this, Script(object : EntityBehaviour() {
                private lateinit var transform: Transform
                private val speed = 1f

                private var eulerAngles = Vector3f()

                override fun start() {
                    transform = getComponent<Transform>()!!
                }

                override fun update() {
                    if(Input.mouseLocked) {
                        eulerAngles.x -= Input.mousePosition.y
                        eulerAngles.y -= Input.mousePosition.x
                        transform.rotation = Quaternion.euler(0f, eulerAngles.y, 0f) * Quaternion.euler(eulerAngles.x, 0f, 0f)

                        val horizontal = Input.getHorizontalAxis() * speed * Time.deltaTime
                        val vertical = Input.getVerticalAxis() * speed * Time.deltaTime
                        transform.position += transform.forward * vertical + transform.right * horizontal

                        var rise = 0f
                        if(Input.isKeyDown(Key.SPACE))
                            rise += speed
                        if(Input.isKeyDown(Key.SHIFT))
                            rise -= speed
                        rise *= Time.deltaTime

                        transform.position.y += rise
                    }
                }
            }))
        }
        createEntity().apply {
            addComponent(this, Transform(this@GameScene, this, position = Vector3f(0f, 0f, -1f)))
            addComponent(this, MeshFilter(MeshFactory.createCube()))
            addComponent(this, MeshRenderer(material = Material(texture = assets.getAsset<Texture>("dirt")), wireframe = false, cullFaces = true))
            addComponent(this, Script(object : EntityBehaviour() {
                private lateinit var transform: Transform
                private val speed = 45f

                private var paused = true

                override fun start() {
                    transform = getComponent<Transform>()!!
                }

                override fun update() {
                    if(Input.isKeyPressed(Key.P))
                        paused = !paused
                    if(!paused)
                        transform.rotation = transform.rotation * Quaternion.euler(speed * Time.deltaTime, speed * Time.deltaTime, 0f)
                }
            }))
        }
        createEntity().apply {
            addComponent(this, Transform(this@GameScene, this, position = Vector3f(0f, 0f, 2f)))
            addComponent(this, MeshFilter(MeshFactory.createPlane()))
            addComponent(this, MeshRenderer(material = Material(texture = assets.getAsset("sun"), lighting = false), cullFaces = false))
            addComponent(this, PointLight(strength = 7f))
            addComponent(this, Script(object: EntityBehaviour() {
                private lateinit var transform: Transform
                private val speed = 1f

                override fun start() {
                    transform = getComponent<Transform>()!!
                }

                override fun update() {
                    if(Input.isKeyPressed(Key.T))
                        transform.position = cameraTransform.position.copy().toVector3f()
                    transform.lookAt(cameraTransform.position)
                }
            }))
        }
        createEntity().apply {
            addComponent(this, AmbientLight())
        }
    }
}