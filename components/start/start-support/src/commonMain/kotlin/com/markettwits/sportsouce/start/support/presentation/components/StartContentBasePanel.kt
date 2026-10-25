package com.markettwits.sportsouce.start.support.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
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
    Column(modifier = modifier.fillMaxWidth().sauceVerticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp).padding(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StartSupportInfoCard()
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
        SauceButton(
            enabled = isEnabled,
            title = "Поддержать",
            onClick = {
                onClickSupport()
                focus.clearFocus()
            }
        )
    }
}
