package editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.border.Border
import kotlin.coroutines.coroutineContext

@Composable
fun SceneView() {
    SwingPanel(
        modifier = Modifier.fillMaxSize(),
        factory = {
            JPanel().apply {
                layout = BorderLayout()
                add(WindowState.window.wrapper, BorderLayout.CENTER)

            }
        }
    )
}