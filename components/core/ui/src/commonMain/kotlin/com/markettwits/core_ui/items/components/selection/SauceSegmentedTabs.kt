package com.markettwits.core_ui.items.components.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.*

/** Short, equal-width groups. Long navigation lists belong in scrollable tabs. */
@Composable
fun SauceSegmentedTabs(
    labels: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    require(labels.size in 2..3) { "Segmented tabs require two or three labels" }
    require(selectedIndex in labels.indices) { "Selected tab must exist" }
    val colors = SauceTheme.colors
    val focus = remember(labels.size) { List(labels.size) { FocusRequester() } }
    val visibility = remember(labels.size) { List(labels.size) { BringIntoViewRequester() } }
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelLarge
    val density = LocalDensity.current
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val available = (maxWidth - SauceSpacing.extraSmall * (labels.size + 1)) / labels.size
        val labelWidths = labels.map { label ->
            with(density) { textMeasurer.measure(label, labelStyle, softWrap = false).size.width.toDp() } + 1.dp
        }
        val scroll = labelWidths.any { width ->
            maxOf(width + SauceSpacing.medium * 2, SauceSizes.touchTarget) > available
        }
        LaunchedEffect(selectedIndex, scroll, maxWidth) {
            if (scroll) {
                withFrameNanos { }
                visibility[selectedIndex].bringIntoView()
            }
        }
        Row(
            Modifier.fillMaxWidth().clip(SauceShapes.button).background(colors.surfaceSubtle)
                .border(1.dp, colors.border, SauceShapes.button)
                .padding(SauceSpacing.extraSmall)
                .then(if (scroll) Modifier.horizontalScroll(rememberScrollState()) else Modifier)
                .height(IntrinsicSize.Min).selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall),
        ) {
            labels.forEachIndexed { index, label ->
                val selected = index == selectedIndex
                val interactions = remember { MutableInteractionSource() }
                val background by animateColorAsState(
                    if (selected && enabled) colors.actionSoft else colors.surfaceSubtle,
                    tween(SauceMotion.feedbackMillis, easing = SauceMotion.feedbackEasing),
                )
                Box(
                    (if (scroll) Modifier.width(IntrinsicSize.Max).widthIn(min = SauceSizes.touchTarget) else Modifier.weight(1f))
                        .fillMaxHeight().heightIn(min = SauceSizes.touchTarget)
                        .clip(SauceShapes.button).background(background)
                        .border(1.dp, when {
                            selected && enabled -> colors.action
                            selected -> colors.border
                            else -> Color.Transparent
                        }, SauceShapes.button)
                        .sauceFocusOutline(interactions, SauceShapes.button, colors.action, enabled)
                        .bringIntoViewRequester(visibility[index])
                        .focusRequester(focus[index])
                        .onPreviewKeyEvent { event ->
                            val direction = when (event.key) {
                                Key.DirectionRight -> if (rtl) -1 else 1
                                Key.DirectionLeft -> if (rtl) 1 else -1
                                else -> 0
                            }
                            if (enabled && direction != 0) {
                                if (event.type == KeyEventType.KeyDown) {
                                    val next = (index + direction + labels.size) % labels.size
                                    focus[next].requestFocus()
                                    onSelected(next)
                                }
                                true
                            } else false
                        }
                        .selectable(selected, enabled = enabled, role = Role.Tab,
                            interactionSource = interactions, indication = androidx.compose.material3.ripple(),
                            onClick = { if (!selected) onSelected(index) })
                        .padding(horizontal = SauceSpacing.medium, vertical = SauceSpacing.small),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(label, modifier = Modifier.width(labelWidths[index]), style = labelStyle, softWrap = false,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = when {
                            !enabled -> colors.disabledContent
                            selected -> colors.onActionSoft
                            else -> colors.textSecondary
                        })
                }
            }
        }
    }
}
