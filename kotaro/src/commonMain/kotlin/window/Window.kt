package dev.pengie.kotaro.window

import dev.pengie.kotaro.ApplicationConfig
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowResizeEvent
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.math.Vector2f
import dev.pengie.kotaro.types.Disposable

abstract class Window(width: Int, height: Int) : Disposable {
    var width: Int = width
        private set
    var height: Int = height
        private set

    internal var mouseLocked = false
        set(value) {
            field = value
            lockMouse(value)
        }

    var mousePosition = Vector2f()
        protected set

    protected var loop: (() -> Unit)? = null
    protected var close: (() -> Unit)? = null

    abstract fun init()
    abstract fun update()
    abstract fun run()
    protected abstract fun resizeWindow(width: Int, height: Int)
    abstract fun getTime(): Double
    abstract fun lockMouse(value: Boolean)

    fun resize(width: Int, height: Int) {
        resizeWindow(width, height)
        this.width = width
        this.height = height
        EventManager.submitEvent(WindowResizeEvent(width, height))
    }

    fun loop(block: () -> Unit) { loop = block }
    fun close(block: () -> Unit) { close = block }

}

expect object WindowFactory {
    operator fun invoke(config: ApplicationConfig): Window
}