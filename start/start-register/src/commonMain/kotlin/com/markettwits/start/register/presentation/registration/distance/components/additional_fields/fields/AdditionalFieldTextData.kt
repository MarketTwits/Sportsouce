package com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimePattern

@Composable
internal fun StartRegistrationAdditionalFieldTextData(
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
            OutlinedTextFieldBase(
                modifier = it,
                label = field.field.title,
                isEnabled = false,
                value = text,
                onValueChange = {}
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
    Spacer(modifier = Modifier.height(8.dp))
}