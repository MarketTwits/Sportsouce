package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
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
    AdditionalFiledTitle(field = field.field)

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
            OutlinedTextFieldBase(
                modifier = it,
                label = "",
                value = time,
                isEnabled = false,
                onValueChange = {}
            )
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
}