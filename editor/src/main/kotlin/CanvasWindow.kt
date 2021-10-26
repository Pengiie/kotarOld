import androidx.compose.ui.unit.dp
import dev.pengie.kotaro.Application
import dev.pengie.kotaro.events.Event
import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.input.*
import dev.pengie.kotaro.events.window.WindowCloseEvent
import dev.pengie.kotaro.events.window.WindowResizeEvent
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.input.Key
import dev.pengie.kotaro.window.Window
import dev.pengie.kotaro.window.glfwKeyMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.awt.AWTGLCanvas
import org.lwjgl.opengl.awt.GLData
import org.lwjgl.opengl.awt.PlatformWin32GLCanvas
import java.awt.event.*
import kotlin.concurrent.thread
import kotlin.math.roundToInt
import kotlin.reflect.KClass

class CanvasWindow : Window(0, 0) {

    internal val wrapper = CanvasWrapper()

    private val time: Double = System.currentTimeMillis().toDouble() / 1000

    init {
        thread {
            Application.invoke(window = this@CanvasWindow, startScene = EDITOR_SCENE, context = {
                wrapper.runInContext {
                    this.wrapper.initGL()
                    it.invoke()
                }
            })
        }
    }

    override fun init() {}
    override fun update() {}
    override fun run() {
        while(Application.isRunning) {
            wrapper.render()
        }
        dispose()
    }

    override fun resizeWindow(width: Int, height: Int) {

    }

    override fun getTime(): Double =
        (System.currentTimeMillis().toDouble() / 1000) - time

    override fun lockMouse(value: Boolean) {

    }

    override fun dispose() {
        close!!.invoke()
        wrapper.disposeCanvas()
    }

    @Suppress("UNCHECKED_CAST")
    internal inner class CanvasWrapper : AWTGLCanvas(GLData().apply {
        this.majorVersion = 3
        this.minorVersion = 2
        this.profile = GLData.Profile.CORE
    }) {
        val queuedEvents: MutableList<EventDescriptor<Event>> = mutableListOf()

        internal inner class EventDescriptor <T : Event> (val event: Event, val type: KClass<T>)

        init {
            addKeyListener(object : KeyAdapter() {
                override fun keyPressed(e: KeyEvent?) {
                    if(e == null)
                        return
                    val key = canvasKeyMap[e.keyCode] ?: return
                    queuedEvents.add(EventDescriptor(KeyDownEvent(key, hashSetOf()), KeyDownEvent::class) as EventDescriptor<Event>)
                }

                override fun keyReleased(e: KeyEvent?) {
                    if(e == null)
                        return
                    val key = canvasKeyMap[e.keyCode] ?: return
                    queuedEvents.add(EventDescriptor(KeyUpEvent(key, hashSetOf()), KeyUpEvent::class) as EventDescriptor<Event>)
                }
            })

            addMouseListener(object : MouseListener {
                override fun mouseClicked(e: MouseEvent?) {}

                override fun mousePressed(e: MouseEvent?) {
                    if(e != null)
                        queuedEvents.add(EventDescriptor(MouseButtonDownEvent(e.button), MouseButtonDownEvent::class) as EventDescriptor<Event>)
                }

                override fun mouseReleased(e: MouseEvent?) {
                    if(e != null)
                        queuedEvents.add(EventDescriptor(MouseButtonUpEvent(e.button), MouseButtonUpEvent::class) as EventDescriptor<Event>)
                }

                override fun mouseEntered(e: MouseEvent?) {}

                override fun mouseExited(e: MouseEvent?) {}

            })

            addMouseMotionListener(object : MouseMotionListener {
                override fun mouseDragged(e: MouseEvent?) {
                    if(e == null)
                        return
                    this@CanvasWindow.mousePosition.x = e.x.toFloat()
                    this@CanvasWindow.mousePosition.y = e.y.toFloat()
                }

                override fun mouseMoved(e: MouseEvent?) {
                    if(e == null)
                        return
                    this@CanvasWindow.mousePosition.x = e.x.toFloat()
                    this@CanvasWindow.mousePosition.y = e.y.toFloat()
                }
            })

            addMouseWheelListener {
                if(it == null)
                    return@addMouseWheelListener
                queuedEvents.add(EventDescriptor(MouseScrollEvent(0f, it.preciseWheelRotation.toFloat()), MouseScrollEvent::class) as EventDescriptor<Event>)
            }
        }

        override fun initGL() {
            GL.createCapabilities()
        }

        override fun paintGL() {
            // Add one to account for flooring values cause the library person decided to do that for some reason
            val w = framebufferWidth + 1
            val h = framebufferHeight + 1
            if(this@CanvasWindow.width != w || this@CanvasWindow.height != h)
                this@CanvasWindow.resize(w, h)
            queuedEvents.forEach { EventManager.submitEvent(it.type, it.event)}
            queuedEvents.clear()
            loop!!.invoke()
            swapBuffers()
        }
    }
}

val canvasKeyMap = hashMapOf(
    Pair(65, Key.A),
    Pair(66, Key.B),
    Pair(67, Key.C),
    Pair(68, Key.D),
    Pair(69, Key.E),
    Pair(70, Key.F),
    Pair(71, Key.G),
    Pair(72, Key.H),
    Pair(73, Key.I),
    Pair(74, Key.J),
    Pair(75, Key.K),
    Pair(76, Key.L),
    Pair(77, Key.M),
    Pair(78, Key.N),
    Pair(79, Key.O),
    Pair(80, Key.P),
    Pair(81, Key.Q),
    Pair(82, Key.R),
    Pair(83, Key.S),
    Pair(84, Key.T),
    Pair(85, Key.U),
    Pair(86, Key.V),
    Pair(87, Key.W),
    Pair(88, Key.X),
    Pair(89, Key.Y),
    Pair(90, Key.Z),
    Pair(32, Key.SPACE),
    Pair(16, Key.SHIFT),
    Pair(17, Key.CTRL),
    Pair(18, Key.ALT),

    Pair(27, Key.ESCAPE)
)