package editor.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import editor.EditorTheme

@Composable
fun SceneExplorer() = Column(modifier = Modifier.fillMaxSize()) {
    Surface(modifier = Modifier.padding(start = 10.dp), elevation = 1.dp, shape = RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp)) {
        Text(modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), text = "Scene Explorer")
    }
    //Divider(modifier = Modifier.fillMaxWidth().height(2.dp), color = EditorTheme.dividerColor)
    Surface(modifier = Modifier.fillMaxSize(), elevation = 2.dp) {
        LazyRow(modifier = Modifier.padding(5.dp)) {

        }
    }
}