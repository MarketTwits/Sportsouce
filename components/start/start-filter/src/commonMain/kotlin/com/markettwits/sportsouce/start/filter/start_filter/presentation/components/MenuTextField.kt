package com.markettwits.sportsouce.start.filter.start_filter.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase

@Composable
fun MenuTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextFieldBase(
        isEnabled = enabled,
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = label,
        maxLines = 1,
        singleLine = true,
        onValueChange = { newValue -> onValueChange(newValue) },
    )
}
