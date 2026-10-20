package com.markettwits.core_ui.items.components.checkbox

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.SauceShapes

@Composable
fun CheckBoxBase(
    checked: Boolean,
    onValueChanged: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(
        checkedColor = SauceTheme.colors.action,
        checkmarkColor = SauceTheme.colors.onAction,
        uncheckedColor = SauceTheme.colors.controlBorder,
        disabledCheckedColor = SauceTheme.colors.disabledContent,
        disabledUncheckedColor = SauceTheme.colors.disabledContent,
        disabledIndeterminateColor = SauceTheme.colors.disabledContent,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Checkbox(
        checked = checked,
        modifier = modifier.sauceFocusOutline(interactionSource, SauceShapes.badge, SauceTheme.colors.action,
            enabled && onValueChanged != null),
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors,
        onCheckedChange = onValueChanged)
}
