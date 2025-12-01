package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldNumber(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    val text = field.answer.number?.toString() ?: ""

    Column(modifier = modifier) {
        OutlinedTextFieldBase(
            value = text,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.all { char -> char.isDigit() }) {
                    onFieldChanged(
                        field.copy(
                            answer = field.answer.copy(
                                number = newValue.toIntOrNull(),
                            )
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = field.field.title,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}