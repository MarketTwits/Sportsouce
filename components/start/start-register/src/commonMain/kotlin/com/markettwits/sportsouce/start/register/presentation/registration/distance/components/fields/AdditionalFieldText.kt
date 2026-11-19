package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldText(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    AdditionalFieldInput(
        modifier = modifier,
        field = field.field,
        value = field.answer.string ?: "",
        onValueChange = {
            onFieldChanged(
                field.copy(
                    answer = field.answer.copy(string = it),
                )
            )
        },
    )
}
