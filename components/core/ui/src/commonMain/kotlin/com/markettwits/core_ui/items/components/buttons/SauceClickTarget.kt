package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme

/** A frameless action: round the feedback without cutting off edge-aligned text or icons. */
@Composable
fun SauceClickTarget(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = SauceShapes.field,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.sizeIn(minWidth = SauceSizes.touchTarget, minHeight = SauceSizes.touchTarget)
        .sauceFocusOutline(interactionSource, shape, SauceTheme.colors.action, enabled)
        .clickable(interactionSource = interactionSource, indication = null,
            enabled = enabled, role = Role.Button, onClickLabel = onClickLabel, onClick = onClick),
        contentAlignment = Alignment.CenterStart) {
        content()
        // Only this empty overlay is clipped. Media can apply its own shape in the content.
        Box(Modifier.matchParentSize().clip(shape).indication(interactionSource, ripple()))
    }
}
