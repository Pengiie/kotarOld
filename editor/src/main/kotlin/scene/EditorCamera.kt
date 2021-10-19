package scene

import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.input.MouseButtonDownEvent
import dev.pengie.kotaro.events.input.MouseButtonUpEvent
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.input.Key
import dev.pengie.kotaro.math.Vector2f
import dev.pengie.kotaro.scene.components.Transform
import dev.pengie.kotaro.script.EntityBehaviour

const val DRAG_SPEED = 0.01f
const val DRAG_START_LENGTH = 10

internal class EditorCamera : EntityBehaviour() {
    private lateinit var transform: Transform

    private val dragStart = Vector2f()
    private val lastDrag = Vector2f()
    private var isDragging = false

    override fun start() {
        transform = getComponent<Transform>()!!

        EventManager.registerListener(EventListener.create<MouseButtonDownEvent> {
            if(Input.isKeyDown(Key.ALT)) {
                isDragging = true
                lastDrag.x = Input.mousePosition.x
                lastDrag.y = Input.mousePosition.y
            }
        })
        EventManager.registerListener(EventListener.create<MouseButtonUpEvent> {
            isDragging = false
        })
    }

    override fun update() {
        if(isDragging) {
            val translation = (Input.mousePosition - lastDrag) * DRAG_SPEED
            lastDrag.x = Input.mousePosition.x
            lastDrag.y = Input.mousePosition.y

            transform.position += transform.right * -translation.x + transform.up * translation.y
        }
    }
}