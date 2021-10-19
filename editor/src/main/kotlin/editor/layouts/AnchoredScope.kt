package editor.layouts

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import editor.layouts.AnchoredScope.anchored

data class AnchorParentData(
    var anchored: Boolean = false,
    var startAnchor: Boolean = false,
    var endAnchor: Boolean = false
)

internal val AnchorParentData?.anchored: Boolean
    get() = this?.anchored ?: false

internal val AnchorParentData?.start: Boolean
    get() = this?.anchored ?: false

internal val AnchorParentData?.end: Boolean
    get() = this?.anchored ?: false

internal class LayoutAnchor(private val anchored: Boolean) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) =
        ((parentData as? AnchorParentData) ?: AnchorParentData()).also {
            it.anchored = anchored
        }
}

internal class LayoutStartAnchor() : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) =
        ((parentData as? AnchorParentData) ?: AnchorParentData()).also {
            it.startAnchor = true
            it.anchored = true
        }
}

internal class LayoutEndAnchor() : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) =
        ((parentData as? AnchorParentData) ?: AnchorParentData()).also {
            it.endAnchor = true
            it.anchored = true
        }
}

object AnchoredScope {
    fun Modifier.anchored(anchored: Boolean = true): Modifier = this.then(LayoutAnchor(anchored))
    fun Modifier.startAnchor(): Modifier = this.then(LayoutStartAnchor())
    fun Modifier.endAnchor(): Modifier = this.then(LayoutEndAnchor())
}