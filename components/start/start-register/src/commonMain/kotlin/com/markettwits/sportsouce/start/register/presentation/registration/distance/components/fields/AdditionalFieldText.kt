package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldText(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    OutlinedTextFieldBase(
        value = field.answer.string ?: "",
        onValueChange = {
            onFieldChanged(
                field.copy(
                    answer = field.answer.copy(string = it),
                )
            )
        },
        modifier = modifier.fillMaxWidth(),
        label = field.field.title
    )
    Spacer(modifier = Modifier.height(8.dp))
}