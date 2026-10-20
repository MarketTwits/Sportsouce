package com.markettwits.core_ui.items.components.focus

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.dp

/** Draw inside the control so focus stays visible in clipped rows without changing its size. */
@Composable
internal fun Modifier.sauceFocusOutline(
    interactionSource: InteractionSource,
    shape: Shape,
    color: Color,
    enabled: Boolean,
): Modifier {
    val focused by interactionSource.collectIsFocusedAsState()
    return if (focused && enabled) drawWithContent {
        drawContent()
        inset(4.dp.toPx()) {
            drawOutline(shape.createOutline(size, layoutDirection, this), color, style = Stroke(2.dp.toPx()))
        }
    } else this
}
