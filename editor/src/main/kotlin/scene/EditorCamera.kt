package scene

import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.input.MouseButtonDownEvent
import dev.pengie.kotaro.events.input.MouseButtonUpEvent
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.input.Key
import dev.pengie.kotaro.math.Quaternion
import dev.pengie.kotaro.math.Vector2f
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.scene.components.Transform
import dev.pengie.kotaro.script.EntityBehaviour

const val DRAG_SPEED = 0.01f
const val ROTATE_SPEED = 0.5f
const val ZOOM_SPEED = 0.3f

internal class EditorCamera : EntityBehaviour() {
    private lateinit var transform: Transform

    private var lastDrag = Vector2f()
    private var lastMouse = Vector2f()
    private var euler = Vector3f()
    private var isDragging = false
    private var isRotating = false

    override fun start() {
        transform = getComponent<Transform>()!!
        euler = transform.rotation.eulerAngles

        EventManager.registerListener(EventListener.create<MouseButtonDownEvent> {
            if(it.button == 2) {
                isDragging = true
                lastDrag.x = Input.mousePosition.x
                lastDrag.y = Input.mousePosition.y
            }
            if(it.button == 3) {
                isRotating = true
                lastMouse = Input.mousePosition.copy() as Vector2f
            }
        })
        EventManager.registerListener(EventListener.create<MouseButtonUpEvent> {
            if(it.button == 2)
                isDragging = false
            if(it.button == 3)
                isRotating = false
        })
    }

    override fun update() {
        // Middle click move
        if(isDragging) {
            val translation = (Input.mousePosition - lastDrag) * DRAG_SPEED
            lastDrag.x = Input.mousePosition.x
            lastDrag.y = Input.mousePosition.y

            transform.position += transform.right * -translation.x + transform.up * translation.y
        }
        // Right click rotate camera
        if(isRotating) {
            val rotation = (Input.mousePosition - lastMouse) * ROTATE_SPEED
            lastMouse = Input.mousePosition.copy() as Vector2f
            euler.x -= rotation.y
            euler.y -= rotation.x
            transform.rotation = Quaternion.euler(0f, euler.y, 0f) * Quaternion.euler(euler.x, 0f, 0f)
        }
        // Scroll zoom
        transform.position += transform.forward * -Input.mouseScroll.y * ZOOM_SPEED
    }
}