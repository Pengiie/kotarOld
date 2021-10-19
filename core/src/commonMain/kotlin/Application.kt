package dev.pengie.kotaro

import dev.pengie.kotaro.events.EventListener
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowCloseEvent
import dev.pengie.kotaro.graphics.RenderSystem
import dev.pengie.kotaro.input.Input
import dev.pengie.kotaro.scene.SceneInstance
import dev.pengie.kotaro.scene.SceneLibrary
import dev.pengie.kotaro.script.ScriptSystem
import dev.pengie.kotaro.window.Window
import dev.pengie.kotaro.window.WindowFactory

object Application {
    var isRunning = true
    lateinit var window: Window
        private set
    lateinit var scene: SceneInstance

    operator fun invoke(
        config: ApplicationConfig = ApplicationConfigFactory.getConfig(),
        window: Window = WindowFactory(config),
        startScene: Int,
        context: (() -> Unit) -> Unit = { it.invoke() }
    ) {
        registerListeners()

        this.window = window
        window.init()


        context.invoke {
            RenderSystem.init()

            this.scene = SceneLibrary[startScene]
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