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

    val text = if (field.answer.number == null) ""
        else field.answer.number.toString()

    Column(modifier = modifier) {
        AdditionalFiledTitle(field = field.field)
        OutlinedTextFieldBase(
            value = text,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    onFieldChanged(
                        field.copy(
                            answer = field.answer.copy(
                                number = it.toInt(),
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