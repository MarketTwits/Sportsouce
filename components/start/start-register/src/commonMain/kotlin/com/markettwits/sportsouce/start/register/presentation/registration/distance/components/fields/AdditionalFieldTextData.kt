package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldTextData(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    val timeMapper = BaseTimeMapper()

    val date = field.answer.date ?: ""

    val text = if (date.isNotEmpty())
        timeMapper.mapTime(time = date, timePattern = TimePattern.FullWithDots)
    else
        ""

    CalendarTextFiled(
        modifier = modifier,
        textFiled = {
            AdditionalFieldInput(
                modifier = it,
                field = field.field,
                value = text,
                placeholder = "Выберите дату",
            )
        },
        onValueChanged = {
            val sent = if (it.isNotEmpty()) timeMapper.mapTimeToCloud(time = it)
                else
                    null
            onFieldChanged(
                field.copy(
                    answer = field.answer.copy(
                        date = sent,
                    )
                )
            )
        }
    )
}
