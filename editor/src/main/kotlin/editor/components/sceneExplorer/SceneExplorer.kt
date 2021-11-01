package editor.components.sceneExplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import editor.EditorState
import editor.components.PropertyPane


@Composable
fun SceneExplorer() = Column(modifier = Modifier.fillMaxSize()) {
    Surface(modifier = Modifier.padding(start = 10.dp), elevation = 1.dp, shape = RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp)) {
        Text(modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), text = "Scene Explorer")
    }
    //Divider(modifier = Modifier.fillMaxWidth().height(2.dp), color = EditorTheme.dividerColor)
    Surface(modifier = Modifier.fillMaxSize(), elevation = 2.dp) {
        with(LocalDensity.current) {
            LazyColumn(modifier = Modifier.padding(start = 12.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)) {
                items(EditorState.sceneList.size) {
                    SceneExplorerItem(14.sp, 14.sp.toDp() * 1.5f, EditorState.sceneList[it])
                }
            }
        }
    }
}

@Composable
private fun SceneExplorerItem(textSize: TextUnit, height: Dp, item: SceneExplorerItem) = Row(
    modifier = Modifier.
    wrapContentHeight().
    clickable {
        EditorState.selectedEntity = item.id
        PropertyPane.newEntity = true
    }.
    background(if(item.id == EditorState.selectedEntity) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.0f)).
    padding(start = 5.dp + 24.dp * item.level).
    height(height).
    fillMaxWidth()
) {
    Text(modifier = Modifier.align(Alignment.CenterVertically), text = item.name, fontSize = textSize)
}