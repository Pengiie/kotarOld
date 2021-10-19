package editor

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val DarkTheme = darkColors(
    surface = Color(0xFF383535)
)

val TypographyTheme = Typography(
    defaultFontFamily = FontFamily.SansSerif,
    h6 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )
)

@Composable
fun EditorTheme(content: @Composable () -> Unit) {
    DesktopMaterialTheme(colors = DarkTheme, typography = TypographyTheme,  content = content)
}

object EditorTheme {
    val dividerColor = Color(0xFF3b3737)
}