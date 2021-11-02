package dev.pengie.kotaro

import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowCloseEvent
import dev.pengie.kotaro.graphics.RenderSystem
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.logging.ConsoleLogger
import dev.pengie.kotaro.logging.LogLevel
import dev.pengie.kotaro.logging.Logger
import dev.pengie.kotaro.logging.LoggerRegistry
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.SceneLibrary
import dev.pengie.kotaro.script.ScriptSystem
import dev.pengie.kotaro.window.Window
import dev.pengie.kotaro.window.WindowFactory

/**
 * The application for the entire game.
 */
object Application {
    var isRunning = true
    lateinit var window: Window
        private set
    lateinit var scene: SceneInstance

    /**
     * Starts the application.
     * @param config the configuration for any settings the application should start with.
     * @param window the window in which the application should use, defaults to platform specific windowing.
     * @param startScene the index of the scene which is the first scene to be launched.
     * @param context the graphics context if it needs explicit running.
     */
    operator fun invoke(
        config: ApplicationConfig = ApplicationConfigFactory.getConfig(),
        window: Window = WindowFactory(config),
        startScene: Int,
        context: (() -> Unit) -> Unit = { it.invoke() },
        loggers: List<Logger> = listOf(ConsoleLogger(LogLevel.Info))
    ) {
        registerListeners()

        loggers.forEach(LoggerRegistry::registerLogger)

        this.window = window
        window.init()
        context.invoke {
            this.scene = SceneLibrary[startScene]
            RenderSystem.init()
            this.scene.assets.load()
            this.scene.init()
        }
        window.loop {
            Time.updateTimings()
            ScriptSystem.update(this.scene)
            RenderSystem.render(this.scene)
            Input.update()
        }
        window.close {
            scene.dispose()
        }

        window.run()
    }

    private fun registerListeners() {
        EventManager.registerListener(EventListener.create<WindowCloseEvent> {
            isRunning = false
        })
    }
}