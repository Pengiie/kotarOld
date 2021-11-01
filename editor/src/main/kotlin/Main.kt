import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.pengie.kotaro.events.EventManager
import dev.pengie.kotaro.events.window.WindowCloseEvent
import editor.Editor
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import welcome.WelcomeWindow

const val EDITOR_SCENE = -1

var openEditor: MutableState<Boolean>? = null
var composeWindow: ComposeWindow? = null

@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking {
    //SceneLibrary.registerScene(EditorScene)

    val defaultState = false


    application {
        openEditor = remember { mutableStateOf(defaultState) }

        if(!(openEditor!!.component1())) {
            Window(
                title = "Kotaro",
                state = WindowState(
                    position = WindowPosition(Alignment.Center),
                    size = WindowSize(800.dp, 600.dp)
                ),
                onCloseRequest = this::exitApplication) {
                composeWindow = this.window
                WelcomeWindow()
            }
        } else {
            Window(title = "Kotaro",
                state = WindowState(
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
}