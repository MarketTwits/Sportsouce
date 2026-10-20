package com.markettwits.core_ui.items.components.selection

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.*

/** Supply an accessible label via the modifier, or use [SauceSwitchRow]. */
@Composable
fun SauceSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = SauceTheme.colors
    val interactions = remember { MutableInteractionSource() }
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.sizeIn(minWidth = SauceSizes.touchTarget, minHeight = SauceSizes.touchTarget)
            .sauceFocusOutline(interactions, SauceShapes.field, colors.action, enabled && onCheckedChange != null),
        enabled = enabled,
        interactionSource = interactions,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.onAction,
            checkedTrackColor = colors.action,
            checkedBorderColor = colors.action,
            uncheckedThumbColor = colors.textMuted,
            uncheckedTrackColor = colors.surfaceSubtle,
            uncheckedBorderColor = colors.controlBorder,
            disabledCheckedThumbColor = colors.disabledContent,
            disabledCheckedTrackColor = colors.disabledContainer,
            disabledCheckedBorderColor = colors.border,
            disabledUncheckedThumbColor = colors.disabledContent,
            disabledUncheckedTrackColor = colors.surfaceSubtle,
            disabledUncheckedBorderColor = colors.border,
        ),
    )
}

/** One toggle target for both the text and the decorative switch, with no duplicate events. */
@Composable
fun SauceSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    enabled: Boolean = true,
) {
    val colors = SauceTheme.colors
    val interactions = remember { MutableInteractionSource() }
    val rowModifier = modifier.fillMaxWidth().heightIn(min = SauceSizes.touchTarget)
        .clip(SauceShapes.field)
        .sauceFocusOutline(interactions, SauceShapes.field, colors.action, enabled)
        .toggleable(checked, interactionSource = interactions, indication = androidx.compose.material3.ripple(),
            enabled = enabled, role = Role.Switch, onValueChange = onCheckedChange)
        .padding(horizontal = SauceSpacing.medium, vertical = SauceSpacing.small)
    val labelContent: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
            Text(label, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall,
                color = if (enabled) colors.textPrimary else colors.disabledContent)
            if (supportingText != null) Text(supportingText, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) colors.textSecondary else colors.disabledContent)
        }
    }
    if (LocalDensity.current.fontScale > 1.3f) {
        Column(rowModifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            labelContent()
            SauceSwitch(checked, onCheckedChange = null, enabled = enabled,
                modifier = Modifier.align(Alignment.End).clearAndSetSemantics {})
        }
    } else {
        Row(rowModifier, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            Box(Modifier.weight(1f)) { labelContent() }
            SauceSwitch(checked, onCheckedChange = null, enabled = enabled, modifier = Modifier.clearAndSetSemantics {})
        }
    }
}
