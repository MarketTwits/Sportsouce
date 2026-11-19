package com.markettwits.sportsouce.start.register.presentation.promo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.material3.MaterialTheme

@Composable
fun RegistrationPromoTextField(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    message: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit
) {

    OutlinedTextFieldBase(
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        label = "Введите промокод",
        isError = isError,
        isEnabled = isEnabled,
        value = value,
        supportingText = {
            if (isError)
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = SauceTheme.colors.danger.content
                )
        },
        trailingIcon = {
            if (isError)
                Icon(Icons.Filled.Error, null, tint = SauceTheme.colors.danger.content)
        },
        maxLines = 1,
        singleLine = true,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}