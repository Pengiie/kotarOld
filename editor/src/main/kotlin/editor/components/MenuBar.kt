package editor.components

import androidx.compose.foundation.ExperimentalDesktopApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp

@Composable
fun MenuBar() {
    Row {
        MenuItem("File")
    }
}

@OptIn(ExperimentalDesktopApi::class)
@Composable
private fun MenuItem(text: String) {
    val hovered = remember { mutableStateOf(false) }

    Box(modifier = Modifier.
    background(if(hovered.value) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.0f)).
    pointerMoveFilter(
        onEnter = {
            hovered.value = true
            false
        },
        onExit = {
            hovered.value = false
            false
        }
    ).
    mouseClickable {
        if(buttons.isPrimaryPressed) {
            // TODO("Menu Popup")
        }
    }) {
        Text(modifier = Modifier.padding(horizontal = 15.dp, vertical = 3.dp), text = text, style = MaterialTheme.typography.h6)
    }
}