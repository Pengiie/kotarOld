package editor.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PropertyPane() {
    Column(modifier = Modifier.fillMaxSize().padding(2.dp)) {
        Surface(elevation = 1.dp, shape = RoundedCornerShape(2.dp)) {
            Text(modifier = Modifier.padding(horizontal = 2.dp, vertical = 1.dp), text = "Properties")
        }
        Surface(modifier = Modifier.fillMaxSize(), elevation = 1.dp) {

        }
    }
}
