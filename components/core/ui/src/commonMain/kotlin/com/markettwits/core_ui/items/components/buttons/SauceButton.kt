package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

/** Variants express prominence; feature screens should not choose arbitrary button colors. */
enum class SauceButtonVariant { Primary, Outlined, Soft, Text, Destructive }

@Composable
fun SauceButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: SauceButtonVariant = SauceButtonVariant.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    fillWidth: Boolean = true,
) {
    val colors = SauceTheme.colors
    val container = when (variant) {
        SauceButtonVariant.Primary -> colors.action
        SauceButtonVariant.Destructive -> colors.material.error
        SauceButtonVariant.Soft -> colors.actionSoft
        SauceButtonVariant.Outlined -> colors.surface
        SauceButtonVariant.Text -> Color.Transparent
    }
    val content = when (variant) {
        SauceButtonVariant.Primary -> colors.onAction
        SauceButtonVariant.Destructive -> colors.material.onError
        SauceButtonVariant.Outlined -> colors.textPrimary
        SauceButtonVariant.Soft, SauceButtonVariant.Text -> colors.action
    }
    ButtonContentBase(
        modifier = modifier,
        title = title,
        onClick = onClick,
        isEnabled = enabled,
        isLoading = loading,
        fillWidth = fillWidth,
        containerColor = container,
        textColor = content,
        borderStroke = if (variant == SauceButtonVariant.Outlined) BorderStroke(1.dp, colors.border) else null,
        disabledContainerColor = if (variant == SauceButtonVariant.Text) Color.Transparent else colors.disabledContainer,
        focusColor = when (variant) {
            SauceButtonVariant.Primary, SauceButtonVariant.Destructive -> content
            else -> colors.action
        },
    )
}
