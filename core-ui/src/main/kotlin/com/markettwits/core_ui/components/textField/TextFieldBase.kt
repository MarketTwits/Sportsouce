package com.markettwits.core_ui.components.textField

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.core_ui.theme.SportSouceTheme

@Composable
fun TextFieldBase(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        enabled = isEnabled,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = {
            Text(text = label, fontFamily = FontNunito.bold, fontSize = 14.sp)
        },
        trailingIcon = {
            if (trailingIcon != null)
                trailingIcon()
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = Color.Gray,
            selectionColors = TextSelectionColors(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.tertiary
            ),
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            errorIndicatorColor = SportSouceColor.SportSouceLightRed,
            errorContainerColor = Color.Transparent,
            errorLabelColor = SportSouceColor.SportSouceLightRed,
            disabledContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Gray,
            disabledLabelColor = Color.Gray,
            disabledTextColor = MaterialTheme.colorScheme.tertiary
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
        TextFieldBase(value = "someText", label = "Some label") {}
    }
}