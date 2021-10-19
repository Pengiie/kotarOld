package editor.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints

@Composable
fun AnchoredRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val placementData = Array(measurables.size) { measurables[it].data }
        val placeables = mutableListOf<Placeable>()
        measurables.indices.filter { placementData[it].anchored }.forEach { placeables.add(measurables[it].measure(constraints)) }
        val positions = Array(measurables.size) { 0 }

        val maxWidth = placeables.sumOf { it.measuredWidth }

        layout(maxWidth, constraints.maxHeight) {

            placeables.forEachIndexed { index, placeable ->
                val centerX = (maxWidth - placeable.width).toFloat() / 2f
                placeable.place(centerX.toInt(), y = 0)
            }
        }
    }
}

private val IntrinsicMeasurable.data: AnchorParentData?
    get() = parentData as? AnchorParentData

