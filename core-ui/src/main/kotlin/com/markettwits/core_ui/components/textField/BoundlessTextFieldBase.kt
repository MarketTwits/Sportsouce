package com.markettwits.core_ui.components.textField

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceTheme

@Composable
fun BoundlessTextFieldBase(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    placeholder: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    TextField(
        enabled = isEnabled,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        value = value,
        trailingIcon = {
            if (trailingIcon != null)
                trailingIcon()
        },
        placeholder = placeholder,
        supportingText = supportingText,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(fontFamily = FontNunito.medium),
        maxLines = maxLines,
        minLines = minLines,
        singleLine = singleLine,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TextFieldBasePreivew() {
    SportSouceTheme {
        OutlinedTextFieldBase(value = "someText", label = "Some label") {}
    }
}