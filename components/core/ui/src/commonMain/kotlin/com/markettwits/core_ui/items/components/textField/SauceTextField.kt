package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import com.markettwits.core_ui.items.theme.*

/** Shared field foundation for native editors and non-editable selector decorations. */
@Composable
fun SauceTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String? = null,
    supportingText: String? = null,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    placeholderContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val colors = SauceTheme.colors
    val interactions = remember { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val errorText = errorMessage?.takeIf { it.isNotBlank() }
    val invalid = isError || errorText != null
    val hint: (@Composable () -> Unit)? = when {
        errorText != null -> { { Text(errorText) } }
        supportingContent != null -> supportingContent
        supportingText != null -> { { Text(supportingText) } }
        else -> null
    }
    val placeholderSlot = placeholderContent ?: placeholder?.let { { Text(it) } }
    val selector = LocalSelectorField.current
    if (selector != null) {
        OutlinedSelectorField(modifier, selector, label, value, invalid, singleLine, maxLines, minLines,
            hint, placeholderSlot, leadingIcon, trailingIcon, visualTransformation, errorText)
        return
    }
    val activation = textFieldActivationModifier(enabled, onClick)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        // The editor carries the accessible name; the visible label must not be read twice.
        Text(label, Modifier.clearAndSetSemantics {}, style = MaterialTheme.typography.labelLarge,
            color = when {
                !enabled -> colors.disabledContent
                invalid -> colors.danger.content
                focused -> colors.action
                else -> colors.textSecondary
            })
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = SauceSizes.fieldMinHeight).then(activation).semantics {
                contentDescription = label
                if (errorText != null) error(errorText)
            },
            enabled = enabled,
            readOnly = readOnly || onClick != null,
            isError = invalid,
            label = null,
            placeholder = placeholderSlot,
            supportingText = hint,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = defaultOutlineTextFiledColors(),
            shape = SauceShapes.field,
            interactionSource = interactions,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }
}
