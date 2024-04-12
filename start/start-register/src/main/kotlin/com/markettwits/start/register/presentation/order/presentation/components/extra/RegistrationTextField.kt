package com.markettwits.start.register.presentation.order.presentation.components.extra

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun RegistrationTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        value = value,
        label = {
            Text(text = label, fontFamily = FontNunito.bold, fontSize = 14.sp)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = SportSouceColor.SportSouceBlue,
            focusedIndicatorColor = SportSouceColor.SportSouceBlue,
            unfocusedIndicatorColor = SportSouceColor.SportSouceBlue,
            selectionColors = TextSelectionColors(
                SportSouceColor.SportSouceBlue,
                SportSouceColor.SportSouceBlue
            ),
            focusedLabelColor = SportSouceColor.SportSouceBlue,
            unfocusedLabelColor = SportSouceColor.SportSouceBlue,
            focusedTextColor = SportSouceColor.SportSouceBlue,
            unfocusedTextColor = SportSouceColor.SportSouceBlue,
            disabledIndicatorColor = SportSouceColor.SportSouceBlue,
            disabledContainerColor = Color.White,
            disabledLabelColor = SportSouceColor.SportSouceBlue,
            disabledTextColor = SportSouceColor.SportSouceBlue
        ),
        textStyle = TextStyle(fontFamily = FontNunito.medium),
        maxLines = 1,
        singleLine = true,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}