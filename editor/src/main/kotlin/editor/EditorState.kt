package editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import editor.components.sceneExplorer.SceneExplorerItem

object EditorState {
    var sceneList by mutableStateOf(listOf<SceneExplorerItem>())
    var selectedEntity: Int? by mutableStateOf(null)
}