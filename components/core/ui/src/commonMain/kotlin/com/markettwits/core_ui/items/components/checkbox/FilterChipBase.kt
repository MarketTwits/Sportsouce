package com.markettwits.core_ui.items.components.checkbox

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.rememberTextMeasurer
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceStatusColors
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline

@Composable
fun FilterChipBase(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    label: String,
    selectedColors: SauceStatusColors? = null,
) {
    val colors = SauceTheme.colors
    val selectedContent = selectedColors?.content ?: colors.onActionSoft
    val selectedContainer = selectedColors?.container ?: colors.actionSoft
    val interactions = remember { MutableInteractionSource() }
    val labelStyle = MaterialTheme.typography.labelLarge
    val density = LocalDensity.current
    val measuredLabel = rememberTextMeasurer().measure(label, style = labelStyle)
    val labelWidth = with(density) { measuredLabel.size.width.toDp() } + 1.dp
    val labelHeight = with(density) { labelStyle.lineHeight.toDp() }
    FilterChip(
        modifier = modifier.heightIn(min = maxOf(SauceSizes.touchTarget, labelHeight + 16.dp))
            .sauceFocusOutline(interactions, SauceShapes.badge, if (selected) selectedContent else colors.action, enabled),
        interactionSource = interactions,
        selected = selected,
        enabled = enabled,
        onClick = onClick,
        shape = SauceShapes.badge,
        label = { Text(text = label, modifier = Modifier.width(labelWidth), style = labelStyle) },
        leadingIcon = if (selected) {
            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
        } else null,
        border = FilterChipDefaults.filterChipBorder(
            enabled = enabled,
            selected = selected,
            borderColor = colors.border,
            selectedBorderColor = selectedContent,
            disabledBorderColor = colors.border,
            disabledSelectedBorderColor = colors.border,
            selectedBorderWidth = 1.dp,
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = colors.surface,
            labelColor = colors.textSecondary,
            iconColor = colors.action,
            disabledContainerColor = colors.disabledContainer,
            disabledLabelColor = colors.disabledContent,
            disabledLeadingIconColor = colors.disabledContent,
            disabledTrailingIconColor = colors.disabledContent,
            selectedContainerColor = selectedContainer,
            selectedLabelColor = selectedContent,
            selectedLeadingIconColor = selectedContent,
            selectedTrailingIconColor = selectedContent,
            disabledSelectedContainerColor = colors.disabledContainer,
        ),
    )
}
