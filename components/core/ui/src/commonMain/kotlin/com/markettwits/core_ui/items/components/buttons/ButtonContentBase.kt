package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline

/** Compatibility API. Custom content inherits the same enabled/disabled foreground as the label. */
@Composable
fun ButtonContentBase(
    modifier: Modifier = Modifier,
    title: String = "",
    onClick: (() -> Unit?)? = null,
    isEnabled: Boolean = true,
    shape: Shape = SauceShapes.button,
    maxLines: Int = Int.MAX_VALUE,
    containerColor: Color = Color.Transparent,
    disabledContainerColor: Color = SauceTheme.colors.disabledContainer,
    textColor: Color = SauceTheme.colors.action,
    borderStroke: BorderStroke? = null,
    content: @Composable (RowScope.() -> Unit)? = null,
    showContent: Boolean = false,
    disabledTextColor: Color = SauceTheme.colors.disabledContent,
    isLoading: Boolean = false,
    fillWidth: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    focusColor: Color = textColor,
) {
    val loading = isLoading
    val interactions = interactionSource ?: remember { MutableInteractionSource() }
    Button(
        modifier = modifier
            .heightIn(min = SauceSizes.touchTarget)
            .sauceFocusOutline(interactions, shape, focusColor, isEnabled && !isLoading)
            .semantics { if (loading) stateDescription = "Загрузка" },
        enabled = isEnabled && !isLoading,
        interactionSource = interactions,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = textColor,
            disabledContainerColor = if (loading) containerColor else disabledContainerColor,
            disabledContentColor = if (loading) textColor else disabledTextColor,
        ),
        shape = shape,
        border = borderStroke,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        onClick = { onClick?.invoke() },
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelLarge) {
            if (content != null && showContent) {
                if (loading) ButtonLoadingIndicator()
                content()
            }
            else Row(
                modifier = if (fillWidth) Modifier.fillMaxWidth() else Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (loading) {
                    ButtonLoadingIndicator()
                }
                Text(
                    modifier = Modifier.weight(1f, fill = false).width(IntrinsicSize.Max),
                    textAlign = TextAlign.Center,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                    text = title,
                )
            }
        }
    }
}

@Composable
private fun ButtonLoadingIndicator() {
    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = LocalContentColor.current, strokeWidth = 2.dp)
    Spacer(Modifier.width(8.dp))
}
