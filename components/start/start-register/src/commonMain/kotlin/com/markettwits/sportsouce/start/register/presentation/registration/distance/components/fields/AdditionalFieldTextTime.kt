package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.textField.TimeTextField
import com.markettwits.core_ui.items.components.timer.domain.parseTimeData
import com.markettwits.core_ui.items.components.timer.screens.rememberTimerPage
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldTextTime(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    val time = field.answer.string ?: ""

    val state = rememberTimerPage(timeState = mutableStateOf(
        parseTimeData(time)
    ))

    TimeTextField(
        modifier = modifier,
        state = state,
        onValueChanged = {
            onFieldChanged(
                field.copy(
                    answer = field.answer.copy(
                        string = it
                    )
                )
        )
        },
        textFiled = {
            AdditionalFieldInput(
                modifier = it,
                field = field.field,
                value = time,
                placeholder = "Выберите время",
            )
        }
    )
}
