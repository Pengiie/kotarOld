package dev.pengie.kotaro.window

import dev.pengie.kotaro.Application
import dev.pengie.kotaro.ApplicationConfig
import dev.pengie.kotaro.DesktopConfig
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.input.KeyDownEvent
import dev.pengie.kotaro.events.input.KeyHeldEvent
import dev.pengie.kotaro.events.input.KeyUpEvent
import dev.pengie.kotaro.events.window.WindowCloseEvent
import dev.pengie.kotaro.events.window.WindowResizeEvent
import dev.pengie.kotaro.graphics.Model
import dev.pengie.kotaro.graphics.RendererFactory
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.input.Key
import dev.pengie.kotaro.input.Modifier
import dev.pengie.kotaro.math.Vector2f
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWWindowCloseCallbackI
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil.*

class DesktopWindow(private val config: DesktopConfig): Window(config.width, config.height) {
    private var handle: Long = NULL

    var lockBlocks = 0
    var lastMousePosition = Vector2f()

    override fun init() {
        GLFWErrorCallback.createPrint(System.err)
        if(!glfwInit())
            throw RuntimeException("Could not initialize GLFW!")
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, if(config.resizable) GLFW_TRUE else GLFW_FALSE )

        handle = glfwCreateWindow(width, height, config.title, NULL, NULL)
        if(handle == NULL)
            throw RuntimeException("Could not create GLFW Window")

        setCallbacks()

        // TODO("Make rendering abstraction for vulkan support")
        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        glfwShowWindow(handle);

        GL.createCapabilities();
        GL11.glViewport(0, 0, width, height);
    }

    private fun setCallbacks() {
        glfwSetWindowCloseCallback(handle) {
            EventManager.submitEvent(WindowCloseEvent())
        }
        glfwSetCursorPosCallback(handle) { _, xpos, ypos ->
            this.mousePosition.x = xpos.toFloat()
            this.mousePosition.y = ypos.toFloat()
            val tempPosition = mousePosition.copy()
            if(mouseLocked) {
                if (lockBlocks <= 0)
                    this.mousePosition -= lastMousePosition
                else {
                    lockBlocks--
                    this.mousePosition = Vector2f()
                }
            }
            lastMousePosition = tempPosition.copy() as Vector2f
        }
        glfwSetKeyCallback(handle) { _, glfwKey, scancode, action, mods ->
            val key = glfwKeyMap[glfwKey] ?: return@glfwSetKeyCallback
            val modifiers = hashSetOf<Modifier>()
            when(action) {
                GLFW_PRESS -> {
                    EventManager.submitEvent(KeyDownEvent(key, modifiers))
                }
                GLFW_REPEAT -> {
                    EventManager.submitEvent(KeyHeldEvent(key, modifiers))
                }
                GLFW_RELEASE -> {
                    EventManager.submitEvent(KeyUpEvent(key, modifiers))
                }
            }
        }
    }

    override fun update() {
        if(this.mouseLocked)
            mousePosition.x(0f).y(0f)
        glfwSwapBuffers(handle)
        glfwPollEvents()
    }

    override fun run() {
        while(Application.isRunning) {
            loop!!.invoke()
            update()
        }
        close!!.invoke()
        dispose()
    }

    override fun resizeWindow(width: Int, height: Int) {
        GL11.glViewport(0, 0, width, height);
    }

    override fun lockMouse(value: Boolean) {
        if(value) {
            lockBlocks = 2
            mousePosition = Vector2f()
            glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
            if(glfwRawMouseMotionSupported())
                glfwSetInputMode(handle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
        } else {
            glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
            glfwSetInputMode(handle, GLFW_RAW_MOUSE_MOTION, GLFW_FALSE)
        }
    }

    override fun getTime(): Double = glfwGetTime()

    override fun dispose() {
        glfwTerminate()
    }
}

actual object WindowFactory {
    actual operator fun invoke(config: ApplicationConfig): Window = DesktopWindow(config as DesktopConfig)
}

val glfwKeyMap = hashMapOf(
    Pair(GLFW_KEY_A, Key.A),
    Pair(GLFW_KEY_B, Key.B),
    Pair(GLFW_KEY_C, Key.C),
    Pair(GLFW_KEY_D, Key.D),
    Pair(GLFW_KEY_E, Key.E),
    Pair(GLFW_KEY_F, Key.F),
    Pair(GLFW_KEY_G, Key.G),
    Pair(GLFW_KEY_H, Key.H),
    Pair(GLFW_KEY_I, Key.I),
    Pair(GLFW_KEY_J, Key.J),
    Pair(GLFW_KEY_K, Key.K),
    Pair(GLFW_KEY_L, Key.L),
    Pair(GLFW_KEY_M, Key.M),
    Pair(GLFW_KEY_N, Key.N),
    Pair(GLFW_KEY_O, Key.O),
    Pair(GLFW_KEY_P, Key.P),
    Pair(GLFW_KEY_Q, Key.Q),
    Pair(GLFW_KEY_R, Key.R),
    Pair(GLFW_KEY_S, Key.S),
    Pair(GLFW_KEY_T, Key.T),
    Pair(GLFW_KEY_U, Key.U),
    Pair(GLFW_KEY_V, Key.V),
    Pair(GLFW_KEY_W, Key.W),
    Pair(GLFW_KEY_X, Key.X),
    Pair(GLFW_KEY_Y, Key.Y),
    Pair(GLFW_KEY_Z, Key.Z),
    Pair(GLFW_KEY_SPACE, Key.SPACE),
    Pair(GLFW_KEY_LEFT_SHIFT, Key.SHIFT),
    Pair(GLFW_KEY_RIGHT_SHIFT, Key.SHIFT),
    Pair(GLFW_KEY_LEFT_CONTROL, Key.CTRL),
    Pair(GLFW_KEY_RIGHT_CONTROL, Key.CTRL),
    Pair(GLFW_KEY_LEFT_ALT, Key.ALT),
    Pair(GLFW_KEY_RIGHT_ALT, Key.ALT),

    Pair(GLFW_KEY_ESCAPE, Key.ESCAPE)
)