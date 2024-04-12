package com.markettwits.start.register.presentation.promo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun RegistrationPromoTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    message: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit
) {

    OutlinedTextFieldBase(
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        label = "Введите промокод",
        value = value,
        supportingText = {
            if (isError)
                Text(
                    text = message,
                    fontFamily = FontNunito.bold,
                    fontSize = 14.sp,
                    color = SportSouceColor.SportSouceLightRed
                )
        },
        trailingIcon = {
            if (isError)
                Icon(Icons.Filled.Error, "error", tint = SportSouceColor.SportSouceLightRed)
        },
        maxLines = 1,
        singleLine = true,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}