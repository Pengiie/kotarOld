import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowCloseEvent
import dev.pengie.kotaro.scene.SceneLibrary
import editor.Editor
import scene.EditorScene
import kotlinx.coroutines.*

const val EDITOR_SCENE = -1
const val GAME_SCENE = 0

@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking {
    SceneLibrary.registerScene(EditorScene)

    application {
        Window(title = "Kotaro",
            state = androidx.compose.ui.window.WindowState(
                placement = WindowPlacement.Maximized
            ),
            onCloseRequest = {
                EventManager.submitEvent(WindowCloseEvent())
                this.exitApplication()
            }) {
            Editor()
        }
    }
}