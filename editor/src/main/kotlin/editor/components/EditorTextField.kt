package editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun EditorTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) =
    BasicTextField(
        textStyle = TextStyle(color = Color.White),
        cursorBrush = SolidColor(Color.White),
        modifier = modifier.
        background(Color.White.copy(alpha = 0.1f)).
        padding(3.dp, 2.dp),
        value = value,
        maxLines = 1,
        onValueChange = onValueChange
    )