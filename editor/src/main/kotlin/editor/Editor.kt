package editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import editor.components.MenuBar
import editor.components.PropertyPane
import editor.components.SceneExplorer
import editor.components.SceneView
import editor.layouts.AnchoredRow
import editor.layouts.AnchoredScope.endAnchor
import editor.layouts.AnchoredScope.startAnchor

@Composable
fun Editor() {
    EditorTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                MenuBar()
                Row {
                    // Left Pane
                    Column(modifier = Modifier.fillMaxWidth(0.25f)) {
                        SceneExplorer()
                    }
                    Divider(modifier = Modifier.width(2.dp).fillMaxHeight(), color = EditorTheme.dividerColor)
                    // Center Pane
                    Column(modifier = Modifier.fillMaxWidth(0.75f).fillMaxHeight()) {
                        SceneView()
                    }
                    Divider(modifier = Modifier.width(2.dp).fillMaxHeight(), color = EditorTheme.dividerColor)
                    // Right Pane
                    Column(modifier = Modifier.fillMaxWidth()) {
                        PropertyPane()
                    }
                }
            }
        }
    }
}