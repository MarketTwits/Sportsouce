package com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields.fields

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
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun StartRegistrationAdditionalFieldNumber(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {

    val text = if (field.answer?.number == null) ""
        else field.answer.number.toString()

    Column(modifier = modifier) {
        StartRegistrationAdditionalFiledTitle(field = field.field)
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