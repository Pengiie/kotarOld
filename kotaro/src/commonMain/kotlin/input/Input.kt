package dev.pengie.kotaro.input

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.input.*
import dev.pengie.kotaro.math.Vector2f

object Input {
    private val keyPress: HashMap<Key, HashSet<Modifier>> = hashMapOf()
    private val keyDown: HashMap<Key, HashSet<Modifier>> = hashMapOf()
    private val keyHeld: HashMap<Key, HashSet<Modifier>> = hashMapOf()
    private val keyRelease: HashMap<Key, HashSet<Modifier>> = hashMapOf()

    private val mousePress: HashSet<Int> = hashSetOf()
    private val mouseDown: HashSet<Int> = hashSetOf()
    private val mouseRelease: HashSet<Int> = hashSetOf()

    /**
     * Determine if the mouse is locked and invisible. If set to true then [mousePosition] is the change in the mouse
     */
    var mouseLocked by Application.window::mouseLocked

    // Local read-only property of the window's mouse position.
    val mousePosition by Application.window::mousePosition

    val mouseScroll = Vector2f()

    fun isKeyPressed(key: Key, vararg modifier: Modifier): Boolean =
        keyPress.containsKey(key) && keyPress[key]!! == modifier.toHashSet()

    fun isKeyDown(key: Key): Boolean =
        keyDown.containsKey(key)

    fun isKeyReleased(key: Key, vararg modifier: Modifier): Boolean =
        keyRelease.containsKey(key) && keyRelease[key]!! == modifier.toHashSet()

    fun isMouseButtonPressed(button: Int) =
        mousePress.contains(button)

    fun isMouseButtonDown(button: Int) =
        mouseDown.contains(button)

    fun isMouseButtonRelease(button: Int) =
        mouseRelease.contains(button)

    fun getHorizontalAxis(): Float {
        if(isKeyDown(Key.D))
            return 1f
        if(isKeyDown(Key.A))
            return -1f
        return 0f
    }

    fun getVerticalAxis(): Float {
        if(isKeyDown(Key.W))
            return 1f
        if(isKeyDown(Key.S))
            return -1f
        return 0f
    }

    private val keyDownListener = EventListener.create<KeyDownEvent> {
        keyPress[it.key] = it.modifiers
        keyDown[it.key] = hashSetOf()
    }

    private val keyUpListener = EventListener.create<KeyUpEvent> {
        keyRelease[it.key] = it.modifiers
        keyDown.remove(it.key)
    }

    private val mouseDownListener = EventListener.create<MouseButtonDownEvent> {
        mousePress.add(it.button)
        mouseDown.add(it.button)
    }

    private val mouseUpListener = EventListener.create<MouseButtonUpEvent> {
        mouseRelease.add(it.button)
        mouseDown.remove(it.button)
    }

    private val mouseScrollListener = EventListener.create<MouseScrollEvent> {
        mouseScroll.x(it.dx).y(it.dy)
    }

    init {
        EventManager.registerListener(keyDownListener)
        EventManager.registerListener(keyUpListener)

        EventManager.registerListener(mouseDownListener)
        EventManager.registerListener(mouseUpListener)
        EventManager.registerListener(mouseScrollListener)
    }

    internal fun update() {
        keyPress.clear()
        keyHeld.clear()
        keyRelease.clear()

        mousePress.clear()
        mouseRelease.clear()

        mouseScroll.x(0f).y(0f)
    }
}