package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme

/**
 * Selectors historically received disabled text fields to suppress the keyboard. Within an
 * anchor, enabled/interaction belong to the selector; the child is a read-only decoration.
 * Ordinary disabled text fields outside this scope retain their original behavior.
 */
// A committed value snapshot for legacy slot-based selectors; read only when opening a dialog.
internal class SelectorFieldValue { var value: String? = null }

internal class SelectorFieldPresentation(
    val enabled: Boolean,
    val interactionSource: MutableInteractionSource,
    val placeholder: String?,
    val fieldValue: SelectorFieldValue?,
    val trailingIcon: (@Composable () -> Unit)?,
)

internal val LocalSelectorField = staticCompositionLocalOf<SelectorFieldPresentation?> { null }

@Composable
internal fun SelectorFieldAnchor(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    expanded: Boolean,
    onOpen: () -> Unit,
    placeholder: String? = null,
    fieldValue: SelectorFieldValue? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val interactions = remember { MutableInteractionSource() }
    Box(
        modifier.sizeIn(minWidth = SauceSizes.touchTarget, minHeight = SauceSizes.touchTarget)
            .clickable(interactionSource = interactions, indication = null, enabled = enabled,
                role = Role.Button, onClickLabel = "Открыть выбор", onClick = onOpen)
            .semantics(mergeDescendants = true) {
                stateDescription = listOfNotNull(placeholder?.takeIf(String::isNotBlank),
                    if (expanded) "Развернуто" else "Свернуто").joinToString(", ")
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        CompositionLocalProvider(LocalSelectorField provides SelectorFieldPresentation(
            enabled, interactions, placeholder, fieldValue, trailingIcon,
        )) { content() }
        // Capture taps above a read-only TextField; keyboard and accessibility use the named
        // parent button. Sharing the source preserves the field's focus/ripple feedback.
        Spacer(Modifier.matchParentSize().clip(SauceShapes.field).clearAndSetSemantics {}.focusProperties { canFocus = false }
            .clickable(interactionSource = interactions, indication = androidx.compose.material3.ripple(color = SauceTheme.colors.action),
                enabled = enabled, onClick = onOpen))
    }
}
