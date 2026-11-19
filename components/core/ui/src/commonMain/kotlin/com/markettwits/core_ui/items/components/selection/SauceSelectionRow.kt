package com.markettwits.core_ui.items.components.selection

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.markettwits.core_ui.items.components.checkbox.CheckBoxBase
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.*

@Composable
fun SauceCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    supportingText: String? = null,
) = SauceSelectionRow(modifier, checked, enabled, Role.Checkbox, onCheckedChange, supportingText) { Text(label, Modifier.fillMaxWidth()) }

/** A label slot preserves formatted prices and required markers in existing forms. */
@Composable
fun SauceCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) = SauceSelectionRow(modifier, checked, enabled, Role.Checkbox, onCheckedChange, null, label)

@Composable
fun SauceRadioRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    supportingText: String? = null,
) = SauceSelectionRow(modifier, selected, enabled, Role.RadioButton, { onClick() }, supportingText) { Text(label, Modifier.fillMaxWidth()) }

/** The row owns the action. Its marker is decorative, so touch and accessibility fire once. */
@Composable
private fun SauceSelectionRow(
    modifier: Modifier,
    checked: Boolean,
    enabled: Boolean,
    role: Role,
    onChange: (Boolean) -> Unit,
    supportingText: String?,
    label: @Composable () -> Unit,
) {
    val colors = SauceTheme.colors
    val interactions = remember { MutableInteractionSource() }
    val selection = if (role == Role.Checkbox) Modifier.toggleable(
        value = checked, enabled = enabled, role = role, onValueChange = onChange,
        interactionSource = interactions, indication = androidx.compose.material3.ripple(),
    ) else Modifier.selectable(
        selected = checked, enabled = enabled, role = role, onClick = { onChange(true) },
        interactionSource = interactions, indication = androidx.compose.material3.ripple(),
    )
    Row(
        modifier.fillMaxWidth().heightIn(min = SauceSizes.touchTarget).clip(SauceShapes.field)
            .sauceFocusOutline(interactions, SauceShapes.field, colors.action, enabled)
            .then(selection).padding(horizontal = SauceSpacing.small, vertical = SauceSpacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
    ) {
        val marker = Modifier.size(SauceSizes.icon).clearAndSetSemantics {}
        if (role == Role.Checkbox) CheckBoxBase(checked, null, marker, enabled)
        else RadioButtonBase(checked, null, marker, enabled)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
            CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides
                if (enabled) colors.textPrimary else colors.disabledContent) {
                ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
                    Box(Modifier.fillMaxWidth(), propagateMinConstraints = true) { label() }
                }
            }
            if (supportingText != null) Text(supportingText, Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) colors.textSecondary else colors.disabledContent)
        }
    }
}
