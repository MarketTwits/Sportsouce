package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline

@Composable
fun BoundlessTextFieldBase(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    val interactions = remember { MutableInteractionSource() }
    TextField(
        enabled = isEnabled,
        readOnly = readOnly || onClick != null,
        isError = isError,
        label = label,
        modifier = modifier.fillMaxWidth().then(textFieldActivationModifier(isEnabled, onClick))
            .sauceFocusOutline(interactions, SauceShapes.field, SauceTheme.colors.action, isEnabled && onClick != null),
        interactionSource = interactions,
        value = value,
        trailingIcon = trailingIcon,
        placeholder = placeholder,
        supportingText = supportingText,
        colors = defaultOutlineTextFiledColors().copy(
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
        shape = SauceShapes.field,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = MaterialTheme.typography.bodyLarge,
        maxLines = maxLines,
        minLines = minLines,
        singleLine = singleLine,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}
