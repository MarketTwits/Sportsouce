package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceMotion
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.bottom_bar.model.BottomNavigationItem
import kotlinx.coroutines.launch
import kotlin.math.hypot

private val revealMotion = tween<Float>(240, easing = SauceMotion.feedbackEasing)

@Composable
internal fun Modifier.navigationTab(
    item: BottomNavigationItem,
    selected: Boolean,
    shape: Shape,
    highlightInset: Dp = 0.dp,
    onClick: () -> Unit,
): Modifier {
    val colors = SauceTheme.colors
    val interactions = remember(item.bottomBarConfiguration) { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val selection by animateFloatAsState(if (selected) 1f else 0f, revealMotion, label = "Navigation selection spread")
    val replay = remember(item.bottomBarConfiguration) { Animatable(1f) }
    val scope = rememberCoroutineScope()

    return clip(shape)
        .drawWithCache {
            val padding = highlightInset.toPx().coerceIn(0f, size.width / 2f)
            val highlightSize = Size(size.width - 2f * padding, size.height)
            val outline = Path().apply { addOutline(shape.createOutline(highlightSize, layoutDirection, this@drawWithCache)) }
            val radius = hypot(highlightSize.width, highlightSize.height) / 2f
            onDrawBehind {
                val fraction = if (selected) minOf(selection, replay.value) else selection
                if (fraction > 0f) {
                    inset(horizontal = padding) {
                        clipPath(outline) {
                            drawCircle(colors.actionSoft.copy(alpha = colors.actionSoft.alpha * fraction), radius * fraction)
                        }
                    }
                }
            }
        }
        .then(if (focused) Modifier.border(1.dp, colors.action, shape) else Modifier)
        .semantics {
            val count = item.badgeCount?.takeIf { it > 0 }
            if (count != null) stateDescription = "Уведомлений: $count"
            else if (item.hasNews) stateDescription = "Есть обновления"
        }
        .selectable(
            selected = selected,
            role = Role.Tab,
            interactionSource = interactions,
            indication = ripple(bounded = false, color = colors.action),
            onClick = {
                if (selected) scope.launch {
                    replay.snapTo(0.14f)
                    replay.animateTo(1f, revealMotion)
                }
                onClick()
            },
        )
}
