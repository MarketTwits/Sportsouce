package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldNumber(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    AdditionalFieldInput(
        modifier = modifier,
        field = field.field,
        value = field.answer.number?.toString().orEmpty(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { newValue ->
            if (newValue.isEmpty() || (newValue.all { it.isDigit() } && newValue.toIntOrNull() != null)) {
                onFieldChanged(field.copy(answer = field.answer.copy(number = newValue.toIntOrNull())))
            }
        },
    )
}
