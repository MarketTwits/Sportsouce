package com.markettwits.core_ui.items.components.selection

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun RadioButtonBase(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = SauceTheme.colors
    val interactions = remember { MutableInteractionSource() }
    RadioButton(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactions,
        modifier = modifier.sauceFocusOutline(interactions, SauceShapes.badge, colors.action,
            enabled && onClick != null),
        colors = RadioButtonDefaults.colors(
            selectedColor = colors.action,
            unselectedColor = colors.controlBorder,
            disabledSelectedColor = colors.disabledContent,
            disabledUnselectedColor = colors.disabledContent,
        ),
    )
}
