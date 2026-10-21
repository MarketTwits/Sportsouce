package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.markettwits.core_ui.items.theme.SauceTheme

/** Compatibility API: all existing form fields now use the shared SauceTextField foundation. */
@Composable
fun OutlinedTextFieldBase(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    supportingText: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit,
) {
    SauceTextField(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        enabled = isEnabled,
        readOnly = readOnly,
        onClick = onClick,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        supportingContent = supportingText,
        placeholderContent = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun defaultOutlineTextFiledColors() = SauceTheme.colors.let { colors ->
    TextFieldDefaults.colors(
        focusedContainerColor = colors.surface,
        unfocusedContainerColor = colors.surface,
        errorContainerColor = colors.surface,
        disabledContainerColor = colors.surfaceSubtle,
        focusedTextColor = colors.textPrimary,
        unfocusedTextColor = colors.textPrimary,
        errorTextColor = colors.textPrimary,
        disabledTextColor = colors.disabledContent,
        cursorColor = colors.action,
        errorCursorColor = colors.danger.content,
        selectionColors = TextSelectionColors(colors.action, colors.actionSoft),
        focusedIndicatorColor = colors.action,
        unfocusedIndicatorColor = colors.controlBorder,
        errorIndicatorColor = colors.danger.content,
        disabledIndicatorColor = colors.border,
        focusedLabelColor = colors.action,
        unfocusedLabelColor = colors.textSecondary,
        errorLabelColor = colors.danger.content,
        disabledLabelColor = colors.disabledContent,
        focusedPlaceholderColor = colors.textMuted,
        unfocusedPlaceholderColor = colors.textMuted,
        disabledPlaceholderColor = colors.disabledContent,
        errorPlaceholderColor = colors.textMuted,
        focusedSupportingTextColor = colors.textMuted,
        unfocusedSupportingTextColor = colors.textMuted,
        errorSupportingTextColor = colors.danger.content,
        disabledSupportingTextColor = colors.disabledContent,
        focusedLeadingIconColor = colors.textSecondary,
        unfocusedLeadingIconColor = colors.textSecondary,
        errorLeadingIconColor = colors.danger.content,
        disabledLeadingIconColor = colors.disabledContent,
        focusedTrailingIconColor = colors.textSecondary,
        unfocusedTrailingIconColor = colors.textSecondary,
        errorTrailingIconColor = colors.danger.content,
        disabledTrailingIconColor = colors.disabledContent,
    )
}
