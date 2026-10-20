package com.markettwits.core_ui.items.components.selection

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.*

/** Navigation tabs for arbitrary-length lists; the caller keeps its existing pager/state. */
@Composable
fun SauceScrollableTabs(
    labels: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: (@Composable (Int) -> Unit)? = null,
    trailingContent: (@Composable (Int) -> Unit)? = null,
) {
    require(labels.isNotEmpty() && selectedIndex in labels.indices) { "Selected tab must exist" }
    val colors = SauceTheme.colors
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val tabMinHeight = maxOf(SauceSizes.touchTarget,
        with(LocalDensity.current) { MaterialTheme.typography.bodyMedium.lineHeight.toDp() } + SauceSpacing.medium * 2)
    val focus = remember(labels.size) { List(labels.size) { FocusRequester() } }
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    SecondaryScrollableTabRow(
        modifier = modifier.heightIn(min = SauceSizes.touchTarget),
        selectedTabIndex = selectedIndex,
        edgePadding = 0.dp,
        divider = {},
        containerColor = Color.Transparent,
        contentColor = colors.textSecondary,
        indicator = {
            TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(selectedIndex, matchContentSize = false),
                height = 2.dp, color = if (enabled) colors.action else colors.disabledContent)
        },
    ) {
        labels.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            val labelStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal)
            // Give text finite bounds inside the horizontal scroller; preserve room for glyph rounding.
            val labelWidth = with(density) {
                textMeasurer.measure(label, labelStyle, softWrap = false, maxLines = 1).size.width.toDp()
            } + 1.dp
            val interactions = remember { MutableInteractionSource() }
            Tab(
                selected = selected,
                enabled = enabled,
                onClick = { if (!selected) onSelected(index) },
                selectedContentColor = if (enabled) colors.action else colors.disabledContent,
                unselectedContentColor = if (enabled) colors.textMuted else colors.disabledContent,
                interactionSource = interactions,
                modifier = Modifier.heightIn(min = tabMinHeight)
                    .sauceFocusOutline(interactions, SauceShapes.field, colors.action, enabled)
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
                    },
            ) {
                Row(Modifier.padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                    leadingContent?.invoke(index)
                    Text(label, modifier = Modifier.width(labelWidth), style = labelStyle,
                        maxLines = 1, softWrap = false)
                    trailingContent?.invoke(index)
                }
            }
        }
    }
}
