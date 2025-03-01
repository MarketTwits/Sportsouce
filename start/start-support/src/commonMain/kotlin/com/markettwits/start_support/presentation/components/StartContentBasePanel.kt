package com.markettwits.start_support.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase

@Composable
internal fun StartSupportPanel(
    modifier: Modifier = Modifier,
    value: String,
    isEnabled: Boolean,
    onValueChanged: (String) -> Unit,
    onClickSupport: () -> Unit
) {
    val focus = LocalFocusManager.current
    Column(modifier = modifier) {
        StartSupportInfoCard()
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextFieldBase(
            label = "Сумма",
            value = value,
            visualTransformation = RubleSymbolVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        ) {
            if (it.length < 7) {
                onValueChanged(it)
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        ButtonContentBase(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            shape = Shapes.medium,
            textColor = MaterialTheme.colorScheme.onSecondary,
            isEnabled = isEnabled,
            title = "Поддержать",
            onClick = {
                onClickSupport()
                focus.clearFocus()
            }
        )
    }
}